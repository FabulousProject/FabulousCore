package me.alpho320.fabulous.core.bukkit;

import me.alpho320.fabulous.core.api.FCore;
import me.alpho320.fabulous.core.api.manager.APIManager;
import me.alpho320.fabulous.core.bukkit.manager.BukkitAPIManager;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitCore implements FCore<Plugin> {

    private final Plugin plugin;
    private APIManager apiManager;

    public BukkitCore(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean init(Plugin initializer) {
        try {

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
        String packageName = plugin.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

}