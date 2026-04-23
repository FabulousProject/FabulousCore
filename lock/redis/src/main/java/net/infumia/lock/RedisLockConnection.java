package net.infumia.lock;

import io.lettuce.core.KeyValue;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.SetArgs;
import io.lettuce.core.Value;
import io.lettuce.core.api.StatefulRedisConnection;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

final class RedisLockConnection {

    private final ReentrantLock lock = new ReentrantLock();
    private final String lockInstanceId;
    private final StatefulRedisConnection<String, String> connection;
    private final Duration expiryTimeout;
    private final Executor executor;

    RedisLockConnection(
        final String lockInstanceId,
        final StatefulRedisConnection<String, String> connection,
        final Duration expiryTimeout,
        final Executor executor
    ) {
        this.lockInstanceId = lockInstanceId;
        this.connection = connection;
        this.expiryTimeout = expiryTimeout;
        this.executor = executor;
    }

    List<String> tryAcquire(final Collection<String> keys) {
        if (keys.size() != 1) {
            return this.script(
                RedisScripts.ACQUIRE,
                keys,
                this.lockInstanceId,
                String.valueOf(this.expiryTimeout.toMillis())
            );
        }
        final String key = keys.iterator().next();
        final String response = this.connection.sync().set(
            key,
            this.lockInstanceId,
            new SetArgs().nx().px(this.expiryTimeout)
        );
        if ("OK".equals(response)) {
            return Collections.singletonList(key);
        } else {
            return Collections.emptyList();
        }
    }

    List<String> tryAcquireOrRenew(final Collection<String> keys) {
        return this.script(
            RedisScripts.ACQUIRE_OR_RENEW_IF_OWNED,
            keys,
            this.lockInstanceId,
            String.valueOf(this.expiryTimeout.toMillis())
        );
    }

    List<String> tryRenew(final Collection<String> keys) {
        return this.script(
            RedisScripts.RENEW_IF_OWNED,
            keys,
            this.lockInstanceId,
            String.valueOf(this.expiryTimeout.toMillis())
        );
    }

    List<String> tryRelease(final Collection<String> keys) {
        return this.script(RedisScripts.DELETE_IF_OWNED, keys, this.lockInstanceId);
    }

    List<String> isOwned(final Collection<String> keys) {
        return this.connection.sync()
            .mget(keys.toArray(new String[0]))
            .stream()
            .filter(Value::hasValue)
            .map(KeyValue::getKey)
            .collect(Collectors.toList());
    }

    <T> CompletableFuture<T> withLockAsync(final Supplier<T> action) {
        return CompletableFuture.supplyAsync(
            () -> {
                this.lock.lock();
                try {
                    return action.get();
                } finally {
                    this.lock.unlock();
                }
            },
            this.executor
        );
    }

    <T> T withLock(final Supplier<T> action) {
        this.lock.lock();
        try {
            return action.get();
        } finally {
            this.lock.unlock();
        }
    }

    List<String> consume(final Collection<String> keys, final Duration consumeTtl) {
        return this.script(
                RedisScripts.CONSUME_IF_OWNED,
                keys,
                this.lockInstanceId,
                String.valueOf(consumeTtl.toMillis())
        );
    }

    private List<String> script(
        final String script,
        final Collection<String> keys,
        final String... parameters
    ) {
        if (keys.isEmpty()) {
            return Collections.emptyList();
        }
        return this.connection.sync().eval(
            script,
            ScriptOutputType.MULTI,
            keys.toArray(new String[0]),
            parameters
        );
    }
}
