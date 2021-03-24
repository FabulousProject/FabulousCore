package me.alpho320.fabulous.core.api.manager.impl.sign;

import me.alpho320.fabulous.core.api.manager.IManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface SignManager extends IManager {

    @NotNull Map<UUID, SignGUI> guiMap();

    @Nullable SignGUI getSign(UUID id);
    @Nullable SignGUI getSign(String id);
    @Nullable SignGUI getSign(Object player);

}