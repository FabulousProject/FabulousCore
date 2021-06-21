package me.alpho320.fabulous.core.bukkit;

import me.alpho320.fabulous.core.api.FCore;
import me.alpho320.fabulous.core.api.manager.APIManager;
import me.alpho320.fabulous.core.api.manager.impl.cooldown.CooldownManager;
import me.alpho320.fabulous.core.api.manager.impl.message.MessageManager;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignManager;
import me.alpho320.fabulous.core.api.manager.impl.worldborder.WorldBorderManager;
import me.alpho320.fabulous.core.api.util.LocationUtil;
import me.alpho320.fabulous.core.api.util.SoundUtil;
import me.alpho320.fabulous.core.bukkit.manager.BukkitAPIManager;
import me.alpho320.fabulous.core.bukkit.util.BukkitLocationUtil;
import me.alpho320.fabulous.core.bukkit.util.BukkitSoundUtil;
import me.alpho320.fabulous.core.bukkit.util.debugger.Debug;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BukkitCore implements FCore<Plugin> {

    private static BukkitCore instance;

    private Plugin plugin;
    private APIManager manager;

    private BukkitLocationUtil serializedLocation;
    private BukkitSoundUtil soundUtil;

    private String version;
    private int versionInt;

    public BukkitCore(Plugin plugin) {
        this.plugin = plugin;
        instance = this;
    }

    @Override
    public boolean init() {
        return init(plugin);
    }

    @Override
    public boolean init(@NotNull String prefix) {
        return init(plugin, prefix);
    }

    @Override
    public boolean init(@NotNull Plugin initializer) {
        return init(initializer, "");
    }

    @Override
    public boolean init(@NotNull Plugin initializer, @NotNull String prefix) {
        try {
            this.plugin = initializer;

            String packageName = plugin.getServer().getClass().getPackage().getName();
            version = packageName.substring(packageName.lastIndexOf('.') + 1);
            versionInt = Integer.parseInt(version.split("[_]")[1]);

            manager = new BukkitAPIManager(this, prefix);
            manager.init();

            this.serializedLocation = new BukkitLocationUtil(this);
            this.soundUtil = new BukkitSoundUtil();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public @NotNull Plugin plugin() {
        return plugin;
    }

    @Override
    public @NotNull APIManager manager() {
        return manager;
    }

    @Override
    public void setAPIManager(@NotNull APIManager manager) {
        this.manager = manager;
    }

    @Override
    public @NotNull String version() {
        return version;
    }

    @Override
    public int versionInt() {
        return versionInt;
    }

    @Override
    public @NotNull Material getSignMaterial(SignGUI.SignType type) {
        Material material = Material.AIR;
        if (type == null) return material;

        try {
            if (versionInt() >= 13) {
                material = Material.matchMaterial(type.name() + "_WALL_SIGN");
                Debug.debug(2, "SignMat: " + material + " (version 1.13+)");
            } else {
                material = Material.matchMaterial("WALL_SIGN");
                Debug.debug(2, "SignMat: " + material + " (version 1.13-)");
            }
        } catch (Exception e) {
            Debug.debug(1, "Sign type of " + type + " is not valid!");
            e.printStackTrace();
        }

        return material == null ? Material.AIR : material;
    }

    @Override
    public @NotNull CooldownManager cooldown() {
        return manager().cooldownManager();
    }

    @Override
    public @NotNull MessageManager message() {
        return manager().messageManager();
    }

    @Override
    public @NotNull SignManager sign() {
        return manager().signManager();
    }

    @Override
    public @NotNull WorldBorderManager border() {
        return manager().worldBorderManager();
    }

    @Override
    public @NotNull LocationUtil location() {
        return serializedLocation;
    }

    @Override
    public @NotNull SoundUtil sound() {
        return soundUtil;
    }

    public static BukkitCore instance() {
        if (instance == null) throw new IllegalStateException("please init!");
        return instance;
    }

}