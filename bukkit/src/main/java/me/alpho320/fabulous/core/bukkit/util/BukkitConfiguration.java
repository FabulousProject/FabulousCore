package me.alpho320.fabulous.core.bukkit.util;

import me.alpho320.fabulous.core.api.util.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BukkitConfiguration extends YamlConfiguration implements Configuration {

    private final @NotNull Plugin plugin;
    private @NotNull File file;

    public BukkitConfiguration(@NotNull String path, @NotNull Plugin plugin) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), path + ".yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            saveDefaults(path);
        }
        reload();
    }

    public BukkitConfiguration(@NotNull String path, @NotNull String target, @NotNull Plugin plugin) {
        this.plugin = plugin;
        Path to = Paths.get(plugin.getDataFolder().toString() + target).toAbsolutePath();

        if (!to.toFile().exists()) {
            if (plugin.getResource(path + ".yml") == null) return;

            file = new File(plugin.getDataFolder(), path + ".yml");
            file.getParentFile().mkdirs();
            saveDefaults(path);

            try {
                Files.move(file.toPath(), to);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            file = new File(plugin.getDataFolder(), target);
        }
        reload();
    }

    @Override
    public void saveDefaults(@NotNull String path) {
        plugin.saveResource(path + ".yml", false);
    }

    public void reload() {
        try {
            super.load(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            super.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public @NotNull File file() {
        return file;
    }

}