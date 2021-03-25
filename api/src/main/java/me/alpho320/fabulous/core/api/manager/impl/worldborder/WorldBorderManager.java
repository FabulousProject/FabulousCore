package me.alpho320.fabulous.core.api.manager.impl.worldborder;

import me.alpho320.fabulous.core.api.manager.IManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface WorldBorderManager extends IManager {

    @NotNull Map<UUID, IWorldBorder> map();

    boolean add(UUID id, IWorldBorder border);
    boolean add(IWorldBorder border);

    boolean remove(UUID id);
    boolean remove(IWorldBorder border);

    @Nullable IWorldBorder get(UUID id);
    @Nullable IWorldBorder get(String id);
    @Nullable IWorldBorder get(Object player);

    boolean contains(UUID id);
    boolean contains(String id);
    boolean contains(IWorldBorder border);

    enum BorderColor {
        RED, GREEN, BLUE;
    }

}