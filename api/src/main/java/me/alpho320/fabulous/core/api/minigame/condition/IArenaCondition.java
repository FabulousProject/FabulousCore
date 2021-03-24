package me.alpho320.fabulous.core.api.minigame.condition;

import me.alpho320.fabulous.core.api.minigame.IArena;
import org.jetbrains.annotations.*;

public interface IArenaCondition {
    @NotNull
    boolean control(@NotNull final IArena p0);
}
