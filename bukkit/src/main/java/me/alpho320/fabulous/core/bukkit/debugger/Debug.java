package me.alpho320.fabulous.core.bukkit.debugger;

import me.alpho320.fabulous.core.bukkit.BukkitCore;
import org.bukkit.plugin.Plugin;

public class Debug {

    private static final Plugin plugin = BukkitCore.instance().plugin();
    private static boolean debug = true;

    public static void debug(int debugType, String message) {
        debug(DebugType.getType(debugType), message);
    }

    public static void debug(DebugType debugType, String message) {
        if (debugType.equals(DebugType.INFO)) {
            plugin.getLogger().info(message);
        } else if (debugType.equals(DebugType.ERROR)) {
            plugin.getLogger().severe(message);
        } else if (debugType.equals(DebugType.OPTIONAL)) {
            if (!debug) return;
            plugin.getLogger().info(message);
        } else {
            throw new IllegalStateException(debugType + " is not valid type of debug!");
        }

    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Debug.debug = debug;
    }

}