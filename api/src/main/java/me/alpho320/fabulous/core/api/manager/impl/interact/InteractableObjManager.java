package me.alpho320.fabulous.core.api.manager.impl.interact;

import me.alpho320.fabulous.core.api.manager.IManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface InteractableObjManager extends IManager {

    @NotNull Map<UUID, InteractableObject<Object>> map();

    boolean add(UUID id, InteractableObject<Object> interactableObject);
    boolean add(InteractableObject<Object> interactableObject);

    boolean remove(UUID id);
    boolean remove(InteractableObject<Object> interactableObject);

    @Nullable InteractableObject<Object> get(UUID id);
    @Nullable InteractableObject<Object> get(String id);
    @Nullable InteractableObject<Object> get(Object id);

    boolean contains(UUID id);
    boolean contains(String id);
    boolean contains(InteractableObject<Object> object);



}