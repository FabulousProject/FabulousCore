package net.infumia.lock;

final class RedisInternal {

    static final String LOCK_PREFIX = "locks::";
    static final String MANUAL_LOCK_PREFIX = "manual_locks::";

    private RedisInternal() {
        throw new IllegalStateException("Utility class");
    }
}
