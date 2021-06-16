package me.alpho320.fabulous.core.api.minigame.arena;

import me.alpho320.fabulous.core.api.minigame.IArena;
import me.alpho320.fabulous.core.api.minigame.IArenaState;
import me.alpho320.fabulous.core.api.minigame.condition.IArenaCondition;
import me.alpho320.fabulous.core.api.minigame.player.ArenaPlayer;
import me.alpho320.fabulous.core.api.minigame.property.IArenaProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicArena implements IArena {

    private final String id;
    private final ArenaType type;
    private IArenaState state;
    private final List<ArenaPlayer> players;
    private final List<IArenaProperty<Object>> properties;
    private final List<IArenaCondition> conditions;
    
    public BasicArena(@NotNull final String id, @NotNull final ArenaType type) {
        this.players = new ArrayList<>();
        this.properties = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.id = id;
        this.type = type;
    }
    
    @NotNull
    @Override
    public String getId() {
        return this.id;
    }
    
    @NotNull
    @Override
    public ArenaType getType() {
        return this.type;
    }
    
    @NotNull
    @Override
    public IArenaState getState() {
        return this.state;
    }
    
    public void setState(@NotNull final IArenaState state) {
        this.state = state;
    }
    
    @NotNull
    @Override
    public List<ArenaPlayer> getPlayers() {
        return this.players;
    }
    
    @NotNull
    @Override
    public List<IArenaProperty<Object>> getProperties() {
        return this.properties;
    }
    
    @NotNull
    @Override
    public List<IArenaCondition> getConditions() {
        return this.conditions;
    }

}