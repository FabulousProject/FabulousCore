package net.infumia.lock;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Represents a distributed lock that provides mutual exclusion across processes and threads.
 * <p>
 * This interface defines the contract for distributed locking operations, supporting both
 * synchronous and asynchronous execution patterns. Implementations are expected to be
 * <strong>non-reentrant</strong>, meaning the same thread cannot acquire the same lock
 * multiple times without first releasing it.
 * </p>
 *
 * <p><strong>Thread Safety:</strong> All implementations must be thread-safe and support
 * concurrent access from multiple threads. However, lock acquisition state is maintained
 * per instance, not per thread.</p>
 *
 * <p><strong>Usage Pattern:</strong></p>
 * <pre>{@code
 * Lock lock = lockProvider.create("my-resource", executor);
 *
 * // Basic usage
 * if (lock.acquire(Duration.ofSeconds(30))) {
 *     try {
 *         // Critical section - access shared resource
 *         performCriticalOperation();
 *     } finally {
 *         lock.release();
 *     }
 * }
 *
 * // Convenience usage
 * lock.withLock(() -> {
 *     performCriticalOperation();
 * });
 *
 * // Asynchronous usage
 * lock.acquireAsync(Duration.ofSeconds(30))
 *     .thenCompose(acquired -> {
 *         if (acquired) {
 *             return performAsyncOperation()
 *                 .whenComplete((result, throwable) -> lock.release());
 *         }
 *         return CompletableFuture.completedFuture(null);
 *     });
 * }</pre>
 *
 * <p><strong>Lock Semantics:</strong></p>
 * <ul>
 *   <li><strong>Non-reentrant:</strong> Same instance cannot acquire lock multiple times</li>
 *   <li><strong>Timeout-based:</strong> Acquisition attempts can timeout</li>
 *   <li><strong>Manual renewal:</strong> Locks can be explicitly renewed to extend expiry</li>
 *   <li><strong>Automatic expiry:</strong> Locks expire after a configurable duration</li>
 * </ul>
 *
 * @see LockProvider
 */
public interface Lock {
    /**
     * Attempts to acquire the lock using the default timeout configured during lock creation.
     * <p>
     * This is a blocking operation that will wait up to the default timeout for the lock
     * to become available. If the lock is already held by this instance, an
     * {@link IllegalStateException} will be thrown as locks are non-reentrant.
     * </p>
     *
     * @return {@code true} if the lock was successfully acquired, {@code false} if the
     *         timeout expired before the lock could be acquired
     * @throws IllegalStateException if this lock instance already holds the lock
     *                              (non-reentrant violation)
     * @throws InterruptedException if the current thread is interrupted while waiting
     * @see #acquire(Duration)
     */
    boolean acquire();

    /**
     * Asynchronously attempts to acquire the lock using the default timeout.
     * <p>
     * This method returns immediately with a {@link CompletableFuture} that will complete
     * when the lock acquisition attempt finishes. The operation is executed on the
     * {@link java.util.concurrent.Executor} provided during lock creation.
     * </p>
     *
     * @return a {@link CompletableFuture} that completes with {@code true} if the lock
     *         was acquired, or {@code false} if the timeout expired
     * @throws IllegalStateException if this lock instance already holds the lock
     *                              (non-reentrant violation)
     * @see #acquireAsync(Duration)
     */
    CompletableFuture<Boolean> acquireAsync();

    /**
     * Attempts to acquire the lock with a specific timeout.
     * <p>
     * This is a blocking operation that will wait up to the specified timeout for the
     * lock to become available. The implementation may use polling with a configurable
     * resolution to check lock availability.
     * </p>
     *
     * @param timeout the maximum time to wait for lock acquisition; must not be null
     *               or negative
     * @return {@code true} if the lock was successfully acquired within the timeout,
     *         {@code false} otherwise
     * @throws IllegalStateException if this lock instance already holds the lock
     *                              (non-reentrant violation)
     * @throws IllegalArgumentException if timeout is null or negative
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    boolean acquire(Duration timeout);

    /**
     * Asynchronously attempts to acquire the lock with a specific timeout.
     * <p>
     * This method returns immediately with a {@link CompletableFuture} that will complete
     * when the lock acquisition attempt finishes within the specified timeout.
     * </p>
     *
     * @param timeout the maximum time to wait for lock acquisition; must not be null
     *               or negative
     * @return a {@link CompletableFuture} that completes with {@code true} if the lock
     *         was acquired within the timeout, or {@code false} otherwise
     * @throws IllegalStateException if this lock instance already holds the lock
     *                              (non-reentrant violation)
     * @throws IllegalArgumentException if timeout is null or negative
     */
    CompletableFuture<Boolean> acquireAsync(Duration timeout);

    /**
     * Renews the lock's expiry time, extending its validity period.
     * <p>
     * This operation can only be performed by the lock instance that currently holds
     * the lock. It resets the lock's expiry time to the original expiry duration
     * configured during lock creation. This is useful for long-running operations
     * that need to maintain the lock beyond its initial expiry time.
     * </p>
     *
     * @return {@code true} if the lock was successfully renewed, {@code false} if
     *         this instance doesn't hold the lock or the renewal failed
     * @see #renewAsync()
     */
    boolean renew();

    /**
     * Asynchronously renews the lock's expiry time.
     * <p>
     * This method returns immediately with a {@link CompletableFuture} that will complete
     * when the renewal operation finishes. Only the lock holder can successfully renew.
     * </p>
     *
     * @return a {@link CompletableFuture} that completes with {@code true} if the lock
     *         was successfully renewed, or {@code false} if this instance doesn't hold
     *         the lock or the renewal failed
     */
    CompletableFuture<Boolean> renewAsync();

    /**
     * Releases the lock, making it available for other processes or threads.
     * <p>
     * This operation can only be performed by the lock instance that currently holds
     * the lock. After successful release, other processes waiting to acquire the same
     * lock may proceed. If this instance doesn't hold the lock, the operation returns
     * {@code false} without error.
     * </p>
     *
     * <p><strong>Important:</strong> Always call this method in a {@code finally} block
     * or use the {@link #withLock(Runnable)} convenience methods to ensure proper
     * lock cleanup.</p>
     *
     * @return {@code true} if the lock was successfully released, {@code false} if
     *         this instance doesn't hold the lock
     * @see #withLock(Runnable)
     * @see #withLock(Supplier)
     */
    boolean release();

    /**
     * Asynchronously releases the lock.
     * <p>
     * This method returns immediately with a {@link CompletableFuture} that will complete
     * when the release operation finishes. Only the lock holder can successfully release.
     * </p>
     *
     * @return a {@link CompletableFuture} that completes with {@code true} if the lock
     *         was successfully released, or {@code false} if this instance doesn't hold
     *         the lock
     */
    CompletableFuture<Boolean> releaseAsync();

    /**
     * Checks whether this lock instance currently holds the lock.
     * <p>
     * This method returns the local state of this lock instance. It does not
     * check the distributed state in the underlying coordination service.
     * The result reflects whether this specific instance believes it holds the lock.
     * </p>
     *
     * @return {@code true} if this instance currently holds the lock, {@code false} otherwise
     * @see #isLockedAsync()
     */
    boolean isLocked();

    /**
     * Asynchronously checks whether this lock instance currently holds the lock.
     *
     * @return a {@link CompletableFuture} that completes with {@code true} if this
     *         instance currently holds the lock, or {@code false} otherwise
     */
    CompletableFuture<Boolean> isLockedAsync();

    boolean consume();
    boolean consume(final int durationSeconds);
    boolean consume(final Duration duration);

    CompletableFuture<Boolean> consumeAsync();
    CompletableFuture<Boolean> consumeAsync(final int durationSeconds);
    CompletableFuture<Boolean> consumeAsync(final Duration duration);


    /**
     * Asynchronously executes an action while holding the lock, with automatic
     * acquisition and release.
     * <p>
     * This convenience method handles the complete lock lifecycle: it attempts to
     * acquire the lock using the default timeout, executes the provided action if
     * acquisition succeeds, and ensures the lock is released regardless of whether
     * the action succeeds or fails.
     * </p>
     *
     * <p><strong>Error Handling:</strong> If lock acquisition fails, the returned
     * {@link CompletableFuture} completes exceptionally with an
     * {@link IllegalStateException}. The lock is guaranteed to be released after
     * action execution, even if the action throws an exception.</p>
     *
     * @param <T> the type of the action's return value
     * @param action the action to execute while holding the lock; must not be null
     * @return a {@link CompletableFuture} that completes with the action's result
     * @throws IllegalArgumentException if action is null
     * @throws IllegalStateException (via CompletableFuture) if lock acquisition fails
     * @see #withLock(Supplier)
     */
    default <T> CompletableFuture<T> withLockAsync(final Supplier<T> action) {
        return this.acquireAsync().thenApply(acquired -> {
                if (!acquired) {
                    throw new IllegalStateException("Failed to acquire the lock!");
                }
                try {
                    return action.get();
                } finally {
                    this.release();
                }
            });
    }

    /**
     * Asynchronously executes a {@link Runnable} action while holding the lock.
     * <p>
     * This is a convenience overload of {@link #withLockAsync(Supplier)} for actions
     * that don't return a value. The lock acquisition, execution, and release cycle
     * is handled automatically.
     * </p>
     *
     * @param action the action to execute while holding the lock; must not be null
     * @return a {@link CompletableFuture} that completes when the action finishes
     * @throws IllegalArgumentException if action is null
     * @throws IllegalStateException (via CompletableFuture) if lock acquisition fails
     * @see #withLockAsync(Supplier)
     * @see #withLock(Runnable)
     */
    default CompletableFuture<?> withLockAsync(final Runnable action) {
        return this.withLockAsync(() -> {
                action.run();
                return null;
            });
    }

    /**
     * Executes an action while holding the lock, with automatic acquisition and release.
     * <p>
     * This convenience method provides a safe way to execute code in a critical section.
     * It attempts to acquire the lock using the default timeout, executes the provided
     * action if acquisition succeeds, and ensures the lock is released in all cases.
     * </p>
     *
     * <p><strong>Thread Safety:</strong> This method is thread-safe and can be called
     * concurrently from multiple threads. However, only one thread will successfully
     * acquire the lock and execute the action.</p>
     *
     * <p><strong>Exception Handling:</strong> The lock is guaranteed to be released
     * even if the action throws an exception. Any exception thrown by the action
     * will propagate to the caller after lock release.</p>
     *
     * @param <T> the type of the action's return value
     * @param action the action to execute while holding the lock; must not be null
     * @return the result of executing the action
     * @throws IllegalArgumentException if action is null
     * @throws IllegalStateException if lock acquisition fails within the default timeout
     * @throws RuntimeException any exception thrown by the action will propagate
     * @see #withLockAsync(Supplier)
     */
    default <T> T withLock(final Supplier<T> action) {
        if (!this.acquire()) {
            throw new IllegalStateException("Failed to acquire the lock!");
        }
        try {
            return action.get();
        } finally {
            this.release();
        }
    }

    /**
     * Executes a {@link Runnable} action while holding the lock.
     * <p>
     * This is a convenience overload of {@link #withLock(Supplier)} for actions
     * that don't return a value. It provides the same automatic lock management
     * and exception safety guarantees.
     * </p>
     *
     * @param action the action to execute while holding the lock; must not be null
     * @throws IllegalArgumentException if action is null
     * @throws IllegalStateException if lock acquisition fails within the default timeout
     * @throws RuntimeException any exception thrown by the action will propagate
     * @see #withLock(Supplier)
     * @see #withLockAsync(Runnable)
     */
    default void withLock(final Runnable action) {
        this.withLock(() -> {
                action.run();
                return null;
            });
    }
}
