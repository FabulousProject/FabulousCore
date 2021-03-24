package me.alpho320.fabulous.core.api.manager.impl.cooldown;

import java.util.UUID;

public class Cooldown {

    private final UUID id;
    private long duration;

    public Cooldown(UUID id, long duration) {
        this.id = id;
        this.duration = duration;
    }

    public UUID getId() {
        return id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}