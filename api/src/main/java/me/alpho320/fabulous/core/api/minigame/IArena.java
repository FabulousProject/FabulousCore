package me.alpho320.fabulous.core.api.minigame;

import me.alpho320.fabulous.core.api.minigame.condition.IArenaCondition;
import me.alpho320.fabulous.core.api.minigame.player.ArenaPlayer;
import me.alpho320.fabulous.core.api.minigame.property.IArenaProperty;
import org.jetbrains.annotations.*;

import java.util.List;

public interface IArena {
    @NotNull
    String getId();
    
    @NotNull
    ArenaType getType();
    
    @NotNull
    IArenaState getState();
    
    @NotNull
    IArenaTimer getTimer();
    
    @NotNull
    List<ArenaPlayer> getPlayers();
    
    @NotNull
    List<IArenaProperty<Object>> getProperties();
    
    @NotNull
    List<IArenaCondition> getConditions();
    
    void join(@NotNull final ArenaPlayer p0);
    void quit(@NotNull final ArenaPlayer p0);

    enum ArenaType {
        SOLO, TEAM;
    }

}
