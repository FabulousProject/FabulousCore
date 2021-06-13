package me.alpho320.fabulous.core.bukkit.manager.impl.sign;

import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignManager;
import me.alpho320.fabulous.core.bukkit.BukkitCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BukkitSignManager implements SignManager {

    private final BukkitCore core;
    private final Map<UUID, SignGUI> map = new HashMap<>();

    private Class<?> bukkitSignGUIClass;

    public BukkitSignManager(BukkitCore core) {
        this.core = core;
    }

    @Override
    public boolean setup() {
        try {

            Class<?> clazz = Class.forName("me.alpho320.fabulous.core.nms.bukkit." + core.version() + ".BukkitSignGUI");

            if (SignGUI.class.isAssignableFrom(clazz)) {
                this.bukkitSignGUIClass = clazz;
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public @NotNull Map<UUID, SignGUI> map() {
        return map;
    }

    @Override
    public boolean add(UUID id, SignGUI gui) {
        if (id == null || gui == null) return false;

        try {
            map.put(id, gui);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean add(SignGUI gui) {
        return add(UUID.randomUUID(), gui);
    }

    @Override
    public boolean remove(UUID id) {
        if (id == null) return false;

        if (map.containsKey(id)) {
            map.remove(id);
            return  true;
        }

        return false;
    }

    @Override
    public boolean remove(SignGUI gui) {
        if (gui == null) return false;
        return remove(gui.id());
    }

    @Override
    public @Nullable SignGUI get(UUID id) {
        if (id == null) return null;
        return map.getOrDefault(id, null);
    }

    @Override
    public @Nullable SignGUI get(String id) {
        if (id == null) return null;

        try {
            return map.getOrDefault(UUID.fromString(id), null);
        } catch (Exception e) {
            return null;
        }

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
            return map.containsKey(UUID.fromString(id));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean contains(SignGUI gui) {
        if (gui == null || gui.id() == null) return false;
        return map.containsKey(gui.id());
    }

}