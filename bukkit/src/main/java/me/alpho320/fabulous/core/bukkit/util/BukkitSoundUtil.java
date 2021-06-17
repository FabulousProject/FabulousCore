package me.alpho320.fabulous.core.bukkit.util;

import me.alpho320.fabulous.core.api.util.SoundUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class BukkitSoundUtil implements SoundUtil<Player, Sound> {

    @Override
    public void send(Player player, Sound sound) {
        send(player, sound, 5);
    }

    @Override
    public void send(Player player, Sound sound, float volume) {
        send(player, sound, volume, false);
    }

    @Override
    public void send(Player player, Sound sound, float volume, boolean nearby) {
        send(player, sound, volume, 1);

        if (nearby) player.getNearbyEntities(10, 2, 10)
                .stream()
                .filter(entity -> entity instanceof Player)
                .forEach(entity -> send((Player) entity, sound, volume, 1));
    }

    public void send(Player player, Sound sound, float volume, float pitch) {
        player.playSound(
                player.getLocation(),
                sound,
                volume,
                pitch
        );
    }

    @Override
    public void send(List<Player> players, Sound sound) {
        players.forEach(player -> send(player, sound));
    }

    @Override
    public void send(List<Player> players, Sound sound, float volume) {
        players.forEach(player -> send(player, sound, volume));
    }

    @Override
    public void send(List<Player> players, Sound sound, float volume, boolean nearby) {
        players.forEach(player -> send(player, sound, volume, nearby));
    }

}