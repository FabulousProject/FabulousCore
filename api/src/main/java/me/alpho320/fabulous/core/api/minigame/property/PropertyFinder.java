package me.alpho320.fabulous.core.api.minigame.property;

import me.alpho320.fabulous.core.api.minigame.IArena;
import org.jetbrains.annotations.*;
import java.util.function.*;

public final class PropertyFinder<T>
{
    public PropertyFinder(@NotNull final Class<T> elementType, @NotNull final IArena arena, @NotNull final Consumer<T> find) {
        this.find(elementType, arena, find);
    }
    
    private final void find(@NotNull final Class<T> elementType, @NotNull final IArena arena, @NotNull final Consumer<T> find) {
        if (arena.getProperties().stream().anyMatch(p -> p.getClass().equals(elementType))) {
            find.accept((T)arena.getProperties().stream().filter(p -> p.getClass().equals(elementType)).findAny().orElse(null));
        }
    }
}
