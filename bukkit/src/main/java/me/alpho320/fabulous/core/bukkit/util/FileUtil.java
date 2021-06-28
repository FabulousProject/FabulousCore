package me.alpho320.fabulous.core.bukkit.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static String removeExtension(String fileName) {
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }

    public static FileConfiguration callFile(JavaPlugin plugin, String path) {
        File file = new File(plugin.getDataFolder(),path + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void saveFile(JavaPlugin plugin, String path, FileConfiguration config) {
        File file = new File(plugin.getDataFolder(),path + ".yml");
        try{
            config.save(file);
        } catch (IOException ignored){ }
    }

    public static void createNewFile(String parent, String child, boolean delete) {
        File file = new File(parent, child);
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (delete) file.delete();
    }

}