package me.alpho320.fabulous.core.nms.bukkit.v1_17_R1;

import me.alpho320.fabulous.core.api.manager.impl.message.IActionBar;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BukkitActionBar implements IActionBar {

    @Override
    public void send(Object player, String text) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), ChatMessageType.c, ((Player)player).getUniqueId());
        ((CraftPlayer) player).getHandle().b.sendPacket(packet);
    }

}