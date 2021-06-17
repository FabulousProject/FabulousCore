package me.alpho320.fabulous.core.bukkit.util;

import me.alpho320.fabulous.core.api.util.SerializedLocation;
import me.alpho320.fabulous.core.bukkit.BukkitCore;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class BukkitSerializedLocation implements SerializedLocation<Location> {

    private final @NotNull BukkitCore core;

    public BukkitSerializedLocation(@NotNull BukkitCore core) {
        this.core = core;
    }

    @Override
    public String serialize(Location loc) {
        return loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getWorld().getName() + ";" + loc.getPitch() + ";" + loc.getYaw();
    }

    @Override
    public List<String> serialize(List<Location> list) {
        return list.stream().map(this::serialize).collect(Collectors.toList());
    }

    @Override
    public Location deserialize(String location) {
        String [] parts = location.split(";");
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        double z = Double.parseDouble(parts[2]);
        String u = parts[3];
        float pitch = Float.parseFloat(parts[4]);
        float yaw = Float.parseFloat(parts[5]);

        return new Location(core.plugin().getServer().getWorld(u), x, y, z, yaw, pitch);
    }

    @Override
    public List<Location> deserialize(List<String> list) {
        return list.stream().map(this::deserialize).collect(Collectors.toList());
    }

}