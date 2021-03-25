package me.alpho320.fabulous.core.api.manager.impl.sign;

import me.alpho320.fabulous.core.api.manager.IManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface SignManager extends IManager {

    @NotNull Map<UUID, SignGUI> map();

    boolean add(UUID id, SignGUI gui);
    boolean add(SignGUI gui);

    boolean remove(UUID id);
    boolean remove(SignGUI gui);

    @Nullable SignGUI get(UUID id);
    @Nullable SignGUI get(String id);
    @Nullable SignGUI get(Object player);

    boolean contains(UUID id);
    boolean contains(String id);
    boolean contains(SignGUI gui);

}