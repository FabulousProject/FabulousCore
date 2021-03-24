package me.alpho320.fabulous.core.api.minigame.condition;

import me.alpho320.fabulous.core.api.minigame.IArena;
import org.jetbrains.annotations.*;
import java.util.function.*;

public final class ConditionFinder<T> {

    public ConditionFinder(@NotNull final Class<T> elementType, @NotNull final IArena arena, @NotNull final Consumer<T> find) {
        this.find(elementType, arena, find);
    }
    
    private void find(@NotNull final Class<T> elementType, @NotNull final IArena arena, @NotNull final Consumer<T> find) {
        if (arena.getConditions().stream().anyMatch(c -> c.getClass().equals(elementType))) {
            find.accept((T)arena.getConditions().stream().filter(c -> c.getClass().equals(elementType)).findAny().orElse(null));
        }
    }
}
