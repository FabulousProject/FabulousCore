package me.alpho320.fabulous.core.api.minigame.player;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface ArenaPlayer {

    @NotNull
    UUID getUUID();
    
    @NotNull
    PlayerType getType();
}
