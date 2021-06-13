package me.alpho320.fabulous.core.nms.bukkit.v1_10_R1;

import me.alpho320.fabulous.core.api.manager.impl.message.IActionBar;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;

public class BukkitActionBar implements IActionBar {

    @Override
    public void send(Object player, String text) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text+ "\"}"), (byte) 2);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

}