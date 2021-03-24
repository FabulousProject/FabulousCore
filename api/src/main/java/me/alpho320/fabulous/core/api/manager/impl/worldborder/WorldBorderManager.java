package me.alpho320.fabulous.core.api.manager.impl.worldborder;

import me.alpho320.fabulous.core.api.manager.IManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface WorldBorderManager extends IManager {

    @NotNull Map<UUID, IWorldBorder> worldBorderMap();

    @Nullable IWorldBorder getWorldBorder(UUID id);
    @Nullable IWorldBorder getWorldBorder(String id);
    @Nullable IWorldBorder getWorldBorder(Object player);

    enum BorderColor {
        RED, GREEN, BLUE;
    }

}