package me.alpho320.fabulous.core.nms.bukkit.v1_21_R5;

import me.alpho320.fabulous.core.api.manager.impl.message.IActionBar;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class BukkitActionBar implements IActionBar {

    @Override
    public void send(Object player, String text) {
        ((Player) player).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
    }

}