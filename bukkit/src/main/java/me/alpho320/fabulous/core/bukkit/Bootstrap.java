package me.alpho320.fabulous.core.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Bootstrap extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("FabulousCore is starting...");
    }

}
