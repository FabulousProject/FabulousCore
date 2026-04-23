package net.infumia.lock;

import java.time.Duration;

final class Internal {

    static final Duration DEFAULT_ACQUIRE_TIMEOUT = Duration.ofSeconds(10L);
    static final Duration DEFAULT_EXPIRY_TIMEOUT = Duration.ofSeconds(60L);
    static final long DEFAULT_ACQUIRE_RESOLUTION_MILLIS = 500L;

    private Internal() {
        throw new IllegalStateException("Utility class");
    }
}
