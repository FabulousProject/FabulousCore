package me.alpho320.fabulous.core.bukkit.manager.impl;

import me.alpho320.fabulous.core.api.manager.impl.interact.InteractableObjManager;
import me.alpho320.fabulous.core.api.manager.impl.interact.InteractableObject;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI;
import me.alpho320.fabulous.core.bukkit.BukkitCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BukkitInteractableObjManager implements InteractableObjManager {

    private final BukkitCore core;
    private final Map<UUID, InteractableObject<Object>> map = new HashMap<>();

    private Class<?> bukkitInteractableObjectClass;

    public BukkitInteractableObjManager(BukkitCore core) {
        this.core = core;
    }

    @Override
    public boolean setup() {
        try {

            Class<?> clazz = Class.forName("me.alpho320.fabulous.core.nms.bukkit" + core.version() + ".BukkitInteractableItem");

            if (SignGUI.class.isAssignableFrom(clazz)) {
                this.bukkitInteractableObjectClass = clazz;
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public @NotNull Map<UUID, InteractableObject<Object>> map() {
        return map;
    }

    @Override
    public boolean add(UUID id, InteractableObject<Object> interactableObject) {
        return false;
    }

    @Override
    public boolean add(InteractableObject<Object> interactableObject) {
        return false;
    }

    @Override
    public boolean remove(UUID id, InteractableObject<Object> interactableObject) {
        return false;
    }

    @Override
    public boolean remove(InteractableObject<Object> interactableObject) {
        return false;
    }

    @Override
    public @Nullable InteractableObject<Object> get(UUID id) {
        return null;
    }

    @Override
    public @Nullable InteractableObject<Object> get(String id) {
        return null;
    }

    @Override
    public @Nullable InteractableObject<Object> get(Object id) {
        return null;
    }

    @Override
    public boolean contains(UUID id) {
        return false;
    }

    @Override
    public boolean contains(String id) {
        return false;
    }

    @Override
    public boolean contains(InteractableObject<Object> object) {
        return false;
    }
}