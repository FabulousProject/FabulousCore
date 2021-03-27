package me.alpho320.fabulous.core.bukkit.manager.impl.interact;

import me.alpho320.fabulous.core.api.manager.impl.interact.InteractableObjManager;
import me.alpho320.fabulous.core.api.manager.impl.interact.InteractableObject;
import me.alpho320.fabulous.core.bukkit.BukkitCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BukkitInteractableObjManager implements InteractableObjManager {

    private final BukkitCore core;
    private final Map<UUID, InteractableObject<Object>> map = new HashMap<>();

    public BukkitInteractableObjManager(BukkitCore core) {
        this.core = core;
    }

    @Override
    public boolean setup() {
        return true;
    }

    @Override
    public @NotNull Map<UUID, InteractableObject<Object>> map() {
        return map;
    }

    @Override
    public boolean add(UUID id, InteractableObject<Object> interactableObject) {
        if (id == null || interactableObject == null) return false;

        map.put(
                id,
                interactableObject
        );
        return true;
    }

    @Override
    public boolean add(InteractableObject<Object> interactableObject) {
        return add(UUID.randomUUID(), interactableObject);
    }

    @Override
    public boolean remove(UUID id) {
        if (id == null) return false;

        if (contains(id)) {
            map.remove(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(InteractableObject<Object> interactableObject) {
        if (interactableObject == null)return false;
        return remove(interactableObject.getId());
    }

    @Override
    public @Nullable InteractableObject<Object> get(UUID id) {
        if (id == null) return null;
        return map.getOrDefault(id, null);
    }

    @Override
    public @Nullable InteractableObject<Object> get(String id) {
        if (id == null) return null;

        try {
            UUID uuid = UUID.fromString(id);
            return get(uuid);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public @Nullable InteractableObject<Object> get(Object id) {
        if (id == null) return null;

        if (id instanceof UUID)
            return get((UUID) id);
        else if (id instanceof String)
            return get((String) id);
        else
            return null;
    }

    @Override
    public boolean contains(UUID id) {
        if (id == null) return false;
        return map.containsKey(id);
    }

    @Override
    public boolean contains(String id) {
        if (id == null) return false;

        try {
            UUID uuid = UUID.fromString(id);
            return contains(uuid);
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public boolean contains(InteractableObject<Object> object) {
        if (object == null) return false;
        return contains(object.getId());
    }

}