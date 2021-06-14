package me.alpho320.fabulous.core.bukkit;

import me.alpho320.fabulous.core.api.FCore;
import me.alpho320.fabulous.core.api.manager.APIManager;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI;
import me.alpho320.fabulous.core.bukkit.debugger.Debug;
import me.alpho320.fabulous.core.bukkit.manager.BukkitAPIManager;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitCore implements FCore<Plugin> {

    private static BukkitCore instance;

    private Plugin plugin;
    private APIManager apiManager;

    private String version;
    private int versionInt;

    private BukkitCore(Plugin plugin) {
        this.plugin = plugin;
        instance = this;
    }

    @Override
    public boolean init(Plugin initializer) {
        if (this.plugin != null) return false;

        try {
            this.plugin = initializer;

            String packageName = plugin.getServer().getClass().getPackage().getName();
            version = packageName.substring(packageName.lastIndexOf('.') + 1);
            versionInt = Integer.parseInt(version.split("[_]")[1]);

            apiManager = new BukkitAPIManager(this);
            apiManager.init();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public @NotNull Plugin plugin() {
        return this.plugin;
    }

    @Override
    public @Nullable APIManager apiManager() {
        return apiManager;
    }

    @Override
    public void setAPIManager(APIManager manager) {
        this.apiManager = manager;
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

    public static BukkitCore instance() {
        if (instance == null) throw new IllegalStateException("please init!");
        return instance;
    }

}