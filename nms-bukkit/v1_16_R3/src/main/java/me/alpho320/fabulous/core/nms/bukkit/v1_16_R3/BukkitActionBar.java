package me.alpho320.fabulous.core.nms.bukkit.v1_16_R3;

import me.alpho320.fabulous.core.api.manager.impl.message.IActionBar;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BukkitActionBar implements IActionBar {

    @Override
    public void send(Object player, String text) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), ChatMessageType.GAME_INFO, ((Player)player).getUniqueId());
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}