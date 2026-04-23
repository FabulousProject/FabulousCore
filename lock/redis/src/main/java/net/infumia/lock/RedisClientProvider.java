package net.infumia.lock;

import io.lettuce.core.RedisClient;

/**
 * Provider interface for obtaining Redis client instances used by the lock system.
 * <p>
 * This interface abstracts the creation and configuration of Redis clients,
 * allowing different implementations to provide clients with varying configurations
 * such as connection pooling, clustering, authentication, and other Redis-specific
 * settings.
 * </p>
 *
 * <p><strong>Implementation Responsibilities:</strong></p>
 * <ul>
 *   <li>Configure and provide properly initialized {@link RedisClient} instances</li>
 *   <li>Handle Redis connection parameters (host, port, authentication, etc.)</li>
 *   <li>Manage client lifecycle and resource cleanup when appropriate</li>
 *   <li>Ensure thread-safe access to the Redis client</li>
 * </ul>
 *
 * <p><strong>Usage Pattern:</strong></p>
 * <pre>{@code
 * // Implementation example
 * public class SimpleRedisClientProvider implements RedisClientProvider {
 *     private final RedisClient client;
 *
 *     public SimpleRedisClientProvider(String host, int port) {
 *         this.client = RedisClient.create(
 *             RedisURI.Builder.redis(host, port).build()
 *         );
 *     }
 *
 *     &#64;Override
 *     public RedisClient provide() {
 *         return client;
 *     }
 * }
 *
 * // Usage
 * RedisClientProvider provider = new SimpleRedisClientProvider("localhost", 6379);
 * RedisLockProvider lockProvider = new RedisLockProvider(provider);
 * }</pre>
 *
 * <p><strong>Connection Management:</strong> Implementations should consider:
 * <ul>
 *   <li>Connection pooling for high-throughput scenarios</li>
 *   <li>Automatic reconnection handling</li>
 *   <li>Proper resource cleanup and shutdown procedures</li>
 *   <li>Connection timeout and retry policies</li>
 * </ul>
 * </p>
 *
 * @see RedisClient
 * @see RedisLockProvider
 */
public interface RedisClientProvider {
    /**
     * Provides a Redis client instance for creating connections to Redis servers.
     * <p>
     * This method should return a properly configured {@link RedisClient} that
     * can be used to establish connections to Redis. The returned client will
     * be used by {@link RedisLockProvider} to create persistent connections
     * for distributed locking operations.
     * </p>
     *
     * <p><strong>Thread Safety:</strong> This method must be thread-safe and
     * can be called concurrently from multiple threads. The returned client
     * should also be thread-safe for concurrent connection creation.</p>
     *
     * <p><strong>Lifecycle:</strong> The returned client should remain valid
     * for the duration of the lock provider's lifecycle. Implementations should
     * not close or dispose of the client while it's being used by lock providers.</p>
     *
     * @return a configured {@link RedisClient} instance ready for creating
     *         connections to Redis servers; must not be null
     * @throws IllegalStateException if the client is not properly configured
     *                              or if connection parameters are invalid
     * @throws io.lettuce.core.RedisConnectionException if the client cannot
     *                                                  connect to Redis during validation
     * @see RedisClient#connect()
     */
    RedisClient provide();
}
