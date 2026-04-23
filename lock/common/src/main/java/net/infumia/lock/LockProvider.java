package net.infumia.lock;

import java.time.Duration;
import java.util.concurrent.Executor;

/**
 * Factory interface for creating {@link Lock} instances with configurable parameters.
 * <p>
 * The {@code LockProvider} follows the factory pattern to abstract the creation and
 * configuration of lock instances. Different implementations can provide locks backed
 * by various coordination services (e.g., Redis, Zookeeper, database) while maintaining
 * a consistent API.
 * </p>
 *
 * <p><strong>Lifecycle Management:</strong> Providers typically manage shared resources
 * such as connections to coordination services. The {@link #initialize()} method must
 * be called before creating any locks to establish these resources.</p>
 *
 * <p><strong>Thread Safety:</strong> Implementations must be thread-safe and support
 * concurrent lock creation from multiple threads after initialization.</p>
 *
 * <p><strong>Usage Pattern:</strong></p>
 * <pre>{@code
 * // Create and initialize the provider
 * LockProvider provider = new RedisLockProvider(clientProvider);
 * provider.initialize();
 *
 * // Create locks with different configurations
 * Executor executor = Executors.newCachedThreadPool();
 *
 * // Basic lock with defaults
 * Lock simpleLock = provider.create("simple-lock", executor);
 *
 * // Lock with custom timeout
 * Lock timeoutLock = provider.create(
 *     "timeout-lock",
 *     executor,
 *     Duration.ofSeconds(30)
 * );
 *
 * // Fully configured lock
 * Lock fullLock = provider.create(
 *     "full-lock",
 *     executor,
 *     Duration.ofSeconds(30),  // acquire timeout
 *     Duration.ofMinutes(5),   // expiry time
 *     100L                     // polling interval
 * );
 * }</pre>
 *
 * <p><strong>Configuration Defaults:</strong> The provider uses sensible defaults
 * defined in {@link Internal} for parameters not explicitly specified in the
 * convenience overloads. See individual {@code create} method documentation for
 * specific default values.</p>
 *
 * @see Lock
 * @see Internal
 */
public interface LockProvider {
    /**
     * Initializes the provider and establishes any necessary connections or resources.
     * <p>
     * This method must be called before any calls to {@link #create} methods.
     * Implementations typically use this method to establish connections to the
     * underlying coordination service, validate configuration, or perform other
     * setup operations.
     * </p>
     *
     * <p><strong>Idempotency:</strong> Multiple calls to this method should be safe
     * and have no additional effect after the first successful call.</p>
     *
     * @throws IllegalStateException if initialization fails due to configuration
     *                              errors or connection issues
     * @throws SecurityException if the provider lacks necessary permissions
     * @see #create(String, Executor, Duration, Duration, long)
     */
    void initialize();

    /**
     * Creates a new lock instance with full parameter specification.
     * <p>
     * This is the primary factory method that allows complete customization of
     * lock behavior. All convenience overloads ultimately delegate to this method
     * with appropriate default values.
     * </p>
     *
     * @param identifier unique identifier for the lock; used as the lock key in the
     *                  coordination service. Must not be null or empty. Multiple
     *                  processes using the same identifier will compete for the same lock
     * @param executor the {@link Executor} used for asynchronous operations; must not
     *                be null. Should typically be a thread pool to avoid blocking
     *                the calling thread during async operations
     * @param acquireTimeout maximum time to wait when attempting to acquire the lock;
     *                      must not be null or negative. If acquisition takes longer
     *                      than this timeout, the operation will fail
     * @param expiryTime how long the lock remains valid after acquisition; must not
     *                  be null or negative. The lock will automatically expire after
     *                  this duration unless renewed. Should be longer than expected
     *                  operation time to prevent premature expiry
     * @param acquireResolutionTimeMillis polling interval in milliseconds when waiting
     *                                   for lock acquisition; must be positive. Smaller
     *                                   values provide faster response but higher overhead
     * @return a new {@link Lock} instance configured with the specified parameters
     * @throws IllegalStateException if the provider is not initialized
     * @throws IllegalArgumentException if any parameter is null, empty, or invalid
     * @see #initialize()
     */
    Lock create(
        String identifier,
        Executor executor,
        Duration acquireTimeout,
        Duration expiryTime,
        long acquireResolutionTimeMillis
    );

    /**
     * Creates a lock with default acquire resolution time.
     * <p>
     * This convenience method uses the default polling interval of 500 milliseconds
     * for lock acquisition attempts.
     * </p>
     *
     * @param identifier unique identifier for the lock; must not be null or empty
     * @param executor executor for asynchronous operations; must not be null
     * @param acquireTimeout maximum time to wait for lock acquisition; must not be null
     * @param expiryTime lock expiry duration; must not be null
     * @return a new {@link Lock} instance with default polling interval
     * @throws IllegalStateException if the provider is not initialized
     * @throws IllegalArgumentException if any parameter is null, empty, or invalid
     * @see #create(String, Executor, Duration, Duration, long)
     */
    default Lock create(
        final String identifier,
        final Executor executor,
        final Duration acquireTimeout,
        final Duration expiryTime
    ) {
        return this.create(
            identifier,
            executor,
            acquireTimeout,
            expiryTime,
            Internal.DEFAULT_ACQUIRE_RESOLUTION_MILLIS
        );
    }

    /**
     * Creates a lock with default expiry time and acquire resolution.
     * <p>
     * This convenience method uses default values for:
     * <ul>
     *   <li>Expiry time: 60 seconds</li>
     *   <li>Acquire resolution: 500 milliseconds</li>
     * </ul>
     * </p>
     *
     * @param identifier unique identifier for the lock; must not be null or empty
     * @param executor executor for asynchronous operations; must not be null
     * @param acquireTimeout maximum time to wait for lock acquisition; must not be null
     * @return a new {@link Lock} instance with default expiry and polling settings
     * @throws IllegalStateException if the provider is not initialized
     * @throws IllegalArgumentException if any parameter is null, empty, or invalid
     * @see #create(String, Executor, Duration, Duration)
     */
    default Lock create(
        final String identifier,
        final Executor executor,
        final Duration acquireTimeout
    ) {
        return this.create(identifier, executor, acquireTimeout, Internal.DEFAULT_EXPIRY_TIMEOUT);
    }

    /**
     * Creates a lock with all default timeout settings.
     * <p>
     * This convenience method uses default values for all timeout parameters:
     * <ul>
     *   <li>Acquire timeout: 10 seconds</li>
     *   <li>Expiry time: 60 seconds</li>
     *   <li>Acquire resolution: 500 milliseconds</li>
     * </ul>
     *
     * This is the simplest way to create a lock when the default timeouts are
     * appropriate for your use case.
     * </p>
     *
     * @param identifier unique identifier for the lock; must not be null or empty
     * @param executor executor for asynchronous operations; must not be null
     * @return a new {@link Lock} instance with all default timeout settings
     * @throws IllegalStateException if the provider is not initialized
     * @throws IllegalArgumentException if identifier is null/empty or executor is null
     * @see #create(String, Executor, Duration)
     */
    default Lock create(final String identifier, final Executor executor) {
        return this.create(identifier, executor, Internal.DEFAULT_ACQUIRE_TIMEOUT);
    }
}
