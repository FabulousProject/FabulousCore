package me.alpho320.fabulous.core.api.minigame;

import org.jetbrains.annotations.NotNull;

public interface IArenaTimer {
    @NotNull
    IArena getArena();
    
    @NotNull
    int getTimeLeft();
    
    @NotNull
    int getTimeElapsed();
    
    void start();
    
    void stop();
}
