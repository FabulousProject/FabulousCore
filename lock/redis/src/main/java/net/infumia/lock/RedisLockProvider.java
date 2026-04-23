package net.infumia.lock;

import io.lettuce.core.api.StatefulRedisConnection;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Executor;

/**
 * Redis-based implementation of {@link LockProvider} that creates distributed locks
 * using Redis as the coordination service.
 * <p>
 * This provider creates {@link RedisLock} instances that use Redis SET operations
 * with expiry times to implement distributed locking. Each lock instance is identified
 * by a unique UUID to ensure proper ownership tracking across different processes.
 * </p>
 *
 * <p><strong>Connection Management:</strong> This provider manages a single Redis
 * connection that is shared across all locks created by this instance. The connection
 * is established during {@link #initialize()} and remains open for the lifetime of
 * the provider.</p>
 *
 * <p><strong>Lock Key Prefix:</strong> All lock keys are automatically prefixed
 * to avoid conflicts with other Redis keys in the same database.</p>
 *
 * <p><strong>Thread Safety:</strong> This class is thread-safe after initialization.
 * Multiple threads can safely call {@link #create} methods concurrently.</p>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>{@code
 * // Create Redis client provider
 * RedisURI uri = RedisURI.Builder.redis("localhost").build();
 * RedisClientProvider clientProvider = new CustomRedisClientProvider(uri);
 *
 * // Create and initialize the lock provider
 * RedisLockProvider lockProvider = new RedisLockProvider(clientProvider);
 * lockProvider.initialize();
 *
 * // Create locks
 * Executor executor = Executors.newCachedThreadPool();
 * Lock lock = lockProvider.create("my-resource", executor);
 *
 * // Use the lock
 * if (lock.acquire(Duration.ofSeconds(30))) {
 *     try {
 *         // Critical section
 *     } finally {
 *         lock.release();
 *     }
 * }
 * }</pre>
 *
 * <p><strong>Redis Requirements:</strong></p>
 * <ul>
 *   <li>Redis 2.6.12 or later (for SET with EX and NX options)</li>
 *   <li>Lua scripting support (for atomic operations)</li>
 *   <li>Persistent connection to Redis server</li>
 * </ul>
 *
 * @see RedisLock
 * @see RedisClientProvider
 * @see LockProvider
 */
public final class RedisLockProvider implements LockProvider {

    private final RedisClientProvider clientProvider;
    private StatefulRedisConnection<String, String> connection;

    /**
     * Creates a new Redis-based lock provider.
     * <p>
     * The provider is created in an uninitialized state. You must call
     * {@link #initialize()} before creating any locks.
     * </p>
     *
     * @param clientProvider the Redis client provider used to establish connections;
     *                      must not be null and should be properly configured
     * @throws IllegalArgumentException if clientProvider is null
     * @see #initialize()
     */
    public RedisLockProvider(final RedisClientProvider clientProvider) {
        this.clientProvider = clientProvider;
    }

    /**
     * Creates a new Redis-based lock provider with an existing connection.
     * <p>
     * The provider is created in an initialized state with the provided connection.
     * You do not need to call {@link #initialize()} when using this constructor.
     * </p>
     *
     * @param connection the Redis connection to use for lock operations;
     *                  must not be null and should be open and ready for use
     * @throws IllegalArgumentException if connection is null
     */
    public RedisLockProvider(final StatefulRedisConnection<String, String> connection) {
        this.clientProvider = null;
        this.connection = connection;
    }

    @Override
    public void initialize() {
        if (this.connection != null) {
            throw new IllegalStateException(
                "RedisLockProvider is already initialized. Cannot initialize twice."
            );
        }
        if (this.clientProvider == null) {
            throw new IllegalStateException(
                "RedisLockProvider was created with a connection. Initialize is not needed."
            );
        }
        this.connection = this.clientProvider.provide().connect();
    }

    @Override
    public Lock create(
        final String identifier,
        final Executor executor,
        final Duration acquireTimeout,
        final Duration expiryTime,
        final long acquireResolutionTimeMillis
    ) {
        if (this.connection == null) {
            throw new IllegalStateException(
                "RedisLockProvider is not initialized. Call initialize() method first."
            );
        }
        return new RedisLock(
            RedisInternal.LOCK_PREFIX + identifier,
            acquireTimeout,
            expiryTime,
            executor,
            acquireResolutionTimeMillis,
            UUID.randomUUID(),
            this.connection
        );
    }
}
