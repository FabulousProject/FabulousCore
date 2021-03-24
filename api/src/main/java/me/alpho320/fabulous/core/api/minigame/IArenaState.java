package me.alpho320.fabulous.core.api.minigame;

import org.jetbrains.annotations.*;

public interface IArenaState {

    int getTime();
    
    @NotNull
    IArenaState getNextState();

}