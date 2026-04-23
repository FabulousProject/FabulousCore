package net.infumia.lock;

import io.lettuce.core.api.StatefulRedisConnection;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Redis-based implementation of the {@link Lock} interface providing distributed
 * locking capabilities across multiple processes and machines.
 * <p>
 * This implementation uses Redis as the coordination service, leveraging Redis's
 * atomic operations and Lua scripting to ensure consistent lock behavior in
 * distributed environments. Each lock instance is identified by a unique UUID
 * to provide proper ownership tracking and prevent accidental releases by
 * different processes.
 * </p>
 *
 * <p><strong>Distributed Lock Semantics:</strong></p>
 * <ul>
 *   <li><strong>Non-reentrant:</strong> A lock instance cannot acquire the same
 *       lock multiple times</li>
 *   <li><strong>Exclusive:</strong> Only one process can hold a given lock at a time</li>
 *   <li><strong>Time-bounded:</strong> Locks automatically expire to prevent deadlocks</li>
 *   <li><strong>Atomic operations:</strong> All Redis operations use Lua scripts
 *       for atomicity</li>
 * </ul>
 *
 * <p><strong>Lock Acquisition Algorithm:</strong></p>
 * <ol>
 *   <li>Attempt to set the lock key in Redis with NX (not exists) flag</li>
 *   <li>If successful, store the unique instance ID as the value</li>
 *   <li>Set expiry time to prevent indefinite lock holding</li>
 *   <li>If unsuccessful, poll at configured intervals until timeout</li>
 * </ol>
 *
 * <p><strong>Thread Safety:</strong> This class is thread-safe. All operations
 * are properly synchronized to prevent race conditions between concurrent
 * operations on the same lock instance.</p>
 *
 * <p><strong>Network Resilience:</strong> The implementation handles common
 * network issues:
 * <ul>
 *   <li>Connection timeouts are handled by the underlying Redis client</li>
 *   <li>Lock expiry prevents indefinite holding if network partitions occur</li>
 *   <li>Ownership verification prevents accidental releases after reconnection</li>
 * </ul>
 * </p>
 *
 * <p><strong>Performance Characteristics:</strong></p>
 * <ul>
 *   <li><strong>Acquisition latency:</strong> Single round-trip to Redis plus polling interval</li>
 *   <li><strong>Memory usage:</strong> Minimal - only stores instance ID in Redis</li>
 *   <li><strong>Network overhead:</strong> One Redis command per operation</li>
 * </ul>
 *
 * @see Lock
 * @see RedisLockProvider
 */
public final class RedisLock implements Lock {

    private final String key;
    private final Duration acquireTimeout;
    private final long acquireResolutionTimeMillis;
    private final RedisLockConnection connection;
    private boolean holdingLock;

    /**
     * Creates a new Redis-based distributed lock.
     * <p>
     * This constructor is typically called by {@link RedisLockProvider} and should
     * not be used directly by application code.
     * </p>
     *
     * @param key the Redis key for this lock (including any necessary prefixes)
     * @param acquireTimeout default timeout for lock acquisition attempts
     * @param expiryTimeout how long the lock remains valid after acquisition
     * @param executor executor for asynchronous operations
     * @param acquireResolutionTimeMillis polling interval for acquisition attempts
     * @param lockInstanceId unique identifier for this lock instance (for ownership tracking)
     * @param connection the Redis connection to use for this lock
     */
    public RedisLock(
        final String key,
        final Duration acquireTimeout,
        final Duration expiryTimeout,
        final Executor executor,
        final long acquireResolutionTimeMillis,
        final UUID lockInstanceId,
        final StatefulRedisConnection<String, String> connection
    ) {
        this.key = key;
        this.acquireTimeout = acquireTimeout;
        this.acquireResolutionTimeMillis = acquireResolutionTimeMillis;

        this.connection = new RedisLockConnection(
            lockInstanceId.toString(),
            connection,
            expiryTimeout,
            executor
        );
    }

    @Override
    public boolean acquire() {
        return this.acquire(this.acquireTimeout);
    }

    @Override
    public CompletableFuture<Boolean> acquireAsync() {
        return this.acquireAsync(this.acquireTimeout);
    }

    @Override
    public boolean acquire(final Duration timeout) {
        return this.connection.withLock(() -> this.acquireSafe(timeout));
    }

    @Override
    public CompletableFuture<Boolean> acquireAsync(final Duration timeout) {
        return this.connection.withLockAsync(() -> this.acquireSafe(timeout));
    }

    @Override
    public boolean renew() {
        return this.connection.withLock(this::renewSafe);
    }

    @Override
    public CompletableFuture<Boolean> renewAsync() {
        return this.connection.withLockAsync(this::renewSafe);
    }

    @Override
    public boolean release() {
        return this.connection.withLock(this::releaseSafe);
    }

    @Override
    public CompletableFuture<Boolean> releaseAsync() {
        return this.connection.withLockAsync(this::releaseSafe);
    }

    @Override
    public boolean isLocked() {
        return this.connection.withLock(() -> this.holdingLock);
    }

    @Override
    public CompletableFuture<Boolean> isLockedAsync() {
        return this.connection.withLockAsync(() -> this.holdingLock);
    }

    @Override
    public boolean consume() {
        return consume(10);
    }

    @Override
    public boolean consume(int durationSeconds) {
        return consume(Duration.ofSeconds(durationSeconds));
    }

    @Override
    public boolean consume(Duration duration) {
        return this.connection.withLock(() -> {
            if (!this.holdingLock) return false;
            boolean ok = !this.connection.consume(
                    Collections.singleton(this.key),
                    duration
            ).isEmpty();
            this.holdingLock = false;
            return ok;
        });
    }

    @Override
    public CompletableFuture<Boolean> consumeAsync() {
        return consumeAsync(10);
    }

    @Override
    public CompletableFuture<Boolean> consumeAsync(int durationSeconds) {
        return consumeAsync(Duration.ofSeconds(durationSeconds));
    }

    @Override
    public CompletableFuture<Boolean> consumeAsync(Duration duration) {
        return this.connection.withLockAsync(() -> {
            if (!this.holdingLock) return false;
            boolean ok = !this.connection.consume(
                    Collections.singleton(this.key),
                    duration
            ).isEmpty();
            this.holdingLock = false;
            return ok;
        });
    }

    private boolean acquireSafe(final Duration timeout) {
        if (this.holdingLock) {
            throw new IllegalStateException("This lock is not reentrant!");
        }
        final Instant deadline = Instant.now().plus(timeout);
        final Set<String> keys = Collections.singleton(this.key);
        while (Instant.now().isBefore(deadline)) {
            if (!this.connection.tryAcquire(keys).isEmpty()) {
                this.holdingLock = true;
                return true;
            }
            try {
                Thread.sleep(this.acquireResolutionTimeMillis);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    private boolean renewSafe() {
        if (!this.holdingLock) {
            return false;
        }
        if (this.connection.tryRenew(Collections.singleton(this.key)).isEmpty()) {
            this.holdingLock = false;
            return false;
        }
        return true;
    }

    private boolean releaseSafe() {
        if (!this.holdingLock) {
            return false;
        }
        final boolean succeed = !this.connection.tryRelease(
            Collections.singleton(this.key)
        ).isEmpty();
        this.holdingLock = false;
        return succeed;
    }
}
