package me.alpho320.fabulous.core.bukkit;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import me.alpho320.fabulous.core.api.FCore;
import me.alpho320.fabulous.core.api.manager.APIManager;
import me.alpho320.fabulous.core.api.manager.impl.cooldown.CooldownManager;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI;
import me.alpho320.fabulous.core.api.manager.impl.worldborder.WorldBorderManager;
import me.alpho320.fabulous.core.api.util.Configuration;
import me.alpho320.fabulous.core.bukkit.manager.BukkitAPIManager;
import me.alpho320.fabulous.core.bukkit.manager.impl.message.BukkitMessageManager;
import me.alpho320.fabulous.core.bukkit.manager.impl.sign.BukkitSignManager;
import me.alpho320.fabulous.core.bukkit.util.BukkitConfiguration;
import me.alpho320.fabulous.core.bukkit.util.BukkitLocationUtil;
import me.alpho320.fabulous.core.bukkit.util.BukkitSoundUtil;
import me.alpho320.fabulous.core.bukkit.util.debugger.Debug;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BukkitCore implements FCore<Plugin> {

    private static BukkitCore instance;

    private Plugin plugin;
    private BukkitAPIManager manager;

    private TaskScheduler taskScheduler;

    private BukkitLocationUtil serializedLocation;
    private BukkitSoundUtil soundUtil;

    private String version;
    private BukkitConfiguration configuration;
    private int versionInt;

    public BukkitCore(Plugin plugin) {
        this.plugin = plugin;
        instance = this;
        this.taskScheduler = UniversalScheduler.getScheduler(plugin);
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
        return init(initializer, prefix, new BukkitConfiguration("messages", plugin));
    }

    @Override
    public boolean init(@NotNull Plugin initializer, @NotNull String prefix, @NotNull Configuration messageConfiguration) {
        try {
            this.plugin = initializer;

            String packageName = plugin.getServer().getClass().getPackage().getName();
            this.version = packageName.substring(packageName.lastIndexOf('.') + 1);

            // for 1.20.5+
            if (version.equalsIgnoreCase("craftbukkit")) {
                String bukkitVersion = plugin.getServer().getMinecraftVersion();
                Debug.debug(0, "BukkitCore | MinecraftVersion: " + bukkitVersion);
                if (bukkitVersion.equals("1.20.4")) {
                    version = "v1_20_R3";
                } else if (bukkitVersion.equals("1.20.6")) {
                    version = "v1_20_R4";
                } else if (bukkitVersion.equals("1.21.1")) {
                    version = "v1_21_R1";
                } else if (bukkitVersion.equals("1.21.2") || bukkitVersion.equals("1.21.3")) {
                    version = "v1_21_R2";
                } else if (bukkitVersion.equals("1.21.4")) {
                    version = "v1_21_R3";
                } else if (bukkitVersion.equals("1.21.5")) {
                    version = "v1_21_R4";
                } else if (bukkitVersion.equals("1.21.6") || bukkitVersion.equals("1.21.7") || bukkitVersion.equals("1.21.8")) {
                    version = "v1_21_R5";
                }
            }

            this.versionInt = Integer.parseInt(plugin.getServer().getBukkitVersion().split("[.]")[1]);

            Debug.debug(0, "BukkitCore | ServerVersion: " + version + " (" + versionInt + ")");

            this.configuration = (BukkitConfiguration) messageConfiguration; // TODO: 21.06.2021 check

            manager = new BukkitAPIManager(this, prefix);
            manager.init();

            this.serializedLocation = new BukkitLocationUtil(this);
            this.soundUtil = new BukkitSoundUtil();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public @NotNull Plugin plugin() {
        return plugin;
    }

    public TaskScheduler taskScheduler() {
        return this.taskScheduler;
    }

    @Override
    public @NotNull BukkitAPIManager manager() {
        return manager;
    }

    @Override
    public void setManager(@NotNull APIManager manager) {
        this.manager = (BukkitAPIManager) manager; // TODO: 21.06.2021 check
    }

    @Override
    public @NotNull BukkitConfiguration messageConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(@NotNull Configuration configuration) {
        this.configuration = (BukkitConfiguration) configuration;
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
    public @NotNull BukkitMessageManager message() {
        return manager().messageManager();
    }

    @Override
    public @NotNull BukkitSignManager sign() {
        return manager().signManager();
    }

    @Override
    public @NotNull WorldBorderManager border() {
        return manager().worldBorderManager();
    }

    @Override
    public @NotNull BukkitLocationUtil location() {
        return serializedLocation;
    }

    @Override
    public @NotNull BukkitSoundUtil sound() {
        return soundUtil;
    }

    public static BukkitCore instance() {
        if (instance == null) throw new IllegalStateException("please init!");
        return instance;
    }

}