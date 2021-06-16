package me.alpho320.fabulous.core.bukkit.manager.impl.message;

import me.alpho320.fabulous.core.api.FCore;
import me.alpho320.fabulous.core.api.manager.impl.message.IActionBar;
import me.alpho320.fabulous.core.api.manager.impl.message.MessageManager;
import me.alpho320.fabulous.core.api.manager.impl.message.MessageType;
import me.alpho320.fabulous.core.bukkit.BukkitCore;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BukkitMessageManager implements MessageManager {

    private final BukkitCore core;
    private final String prefix;

    private IActionBar actionBar;

    public BukkitMessageManager(BukkitCore core, String prefix) {
        this.core = core;
        this.prefix = prefix;
    }

    @Override
    public boolean setup() {

        try {
            PlaceholderAPI.getPlaceholderPattern();
        } catch (Exception e) {
            core.plugin().getLogger().warning("BukkitMessageManager - PlaceholderAPI not found!");
            return false;
        }

        try {

            Class<?> clazz = Class.forName("me.alpho320.fabulous.core.nms.bukkit." + core.version() + ".BukkitActionBar");

            if (IActionBar.class.isAssignableFrom(clazz)) {
                this.actionBar = (IActionBar) clazz.getConstructor().newInstance();
                return true;
            }
            core.plugin().getLogger().warning(core.version() + " is not valid version!");
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public @NotNull FCore core() {
        return core;
    }

    @Override
    public @NotNull String colored(@NotNull String message) {
        if(core.version().equals("v1_16_R3")) {

            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(message);

            while (matcher.find()) {
                String color = message.substring(matcher.start(), matcher.end());
                message = message.replace(color, ChatColor.of(color) + "");
                matcher = pattern.matcher(message);
            }
        }

        return ChatColor.translateAlternateColorCodes('&', message)
                .replace("%prefix%", prefix);
    }

    @Override
    public @NotNull List<String> colored(@NotNull List<String> list) {
        return list.stream().map(this::colored).collect(Collectors.toList());
    }

    @Override
    public @NotNull String withPlaceholders(@NotNull Object player, @NotNull String text) {
        return PlaceholderAPI.setPlaceholders((Player) player, text);
    }

    @Override
    public @NotNull List<String> withPlaceholders(@NotNull Object player, @NotNull List<String> list) {
        return list.stream().map(text -> withPlaceholders(player, text)).collect(Collectors.toList());
    }

    @Override
    public @NotNull String coloredWithPlaceholders(@NotNull Object player, String text) {
        return colored(
                withPlaceholders(
                        player,
                        text
                )
        );
    }

    @Override
    public @NotNull List<String> coloredWithPlaceholders(@NotNull Object player, List<String> list) {
        return list.stream().map(text -> colored(withPlaceholders(player, text))).collect(Collectors.toList());
    }

    @Override
    public void sendMessage(Object sender, String message, MessageType type) {
        if (sender instanceof Player) {
            if (type == MessageType.CHAT) {
                ((Player) sender).sendMessage(coloredWithPlaceholders(sender, message));
            } else if (type == MessageType.TITLE) {
                ((Player) sender).sendTitle(prefix, coloredWithPlaceholders(sender, message));
            } else if (type == MessageType.ACTIONBAR) {
                actionBar.send(sender, coloredWithPlaceholders(sender, message));
            } else {
                throw new IllegalArgumentException(type + " is not valid a MessageType");
            }
        } else if (sender instanceof ConsoleCommandSender) {
            ((ConsoleCommandSender)sender).sendMessage(message);
        }
    }

    @Override
    public void sendMessage(Object sender, List<String> messages, MessageType type) {
        messages.forEach(text -> sendMessage(sender, text, type));
    }

    @Override
    public void sendMessage(Object sender, String message, MessageType type, String[] regex, String[] replacement) {
        sendMessage(sender, replace(message, regex, replacement), type);
    }

    @Override
    public void sendMessage(Object sender, List<String> message, MessageType type, String[] regex, String[] replacement) {
        message.forEach(text -> sendMessage(sender, replace(text, regex, replacement), type));
    }

    @Override
    public void sendTimerMessage(Object player, String message, MessageType type) {
        sendTimerMessage(player, message, type, 5);
    }

    @Override
    public void sendTimerMessage(Object player, String message, MessageType type, int time) {
        sendTimerMessage(player, message, type, time, 20);
    }

    @Override
    public void sendTimerMessage(Object player, String message, MessageType type, int time, long period) {
        AtomicInteger i = new AtomicInteger();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (i.get() > 0) {
                    sendMessage(player, message.replaceAll("%time%", String.valueOf(i.get())), type);
                } else {
                    cancel();
                }
                i.getAndIncrement();
            }
        }.runTaskTimerAsynchronously(core.plugin(), 0, period);
    }

    @Override
    public void sendTimerMessage(Object player, List<String> messages, MessageType type, int time) {
        sendTimerMessage(player, messages, type, time, 20L);
    }

    @Override
    public void sendTimerMessage(Object player, List<String> messages, MessageType type, int time, long period) {
        AtomicInteger i = new AtomicInteger();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (i.get() > 0) {
                    sendMessage(
                            player,
                            replace(
                                    messages,
                                    new String[]{"%time%"},
                                    new String[]{String.valueOf(i.get())}
                                    ),
                            type
                    );
                } else {
                    cancel();
                }
                i.getAndIncrement();
            }
        }.runTaskTimerAsynchronously(core.plugin(), 0, period);
    }

    @Override
    public void sendTimerMessage(Object player, String message, MessageType type, String[] regex, String[] replacement, int time) {
        sendTimerMessage(player, replace(message, regex, replacement), type, time);
    }

    @Override
    public void sendTimerMessage(Object player, String message, MessageType type, String[] regex, String[] replacement, int time, long period) {
        sendTimerMessage(player, replace(message, regex, replacement), type, time, period);
    }

    @Override
    public void sendTimerMessage(Object player, List<String> messages, MessageType type, String[] regex, String[] replacement, int time) {
        sendTimerMessage(player, replace(messages, regex, replacement), type, time);
    }

    @Override
    public void sendTimerMessage(Object player, List<String> messages, MessageType type, String[] regex, String[] replacement, int time, long period) {
        sendTimerMessage(player, replace(messages, regex, replacement), type, time, period);
    }

}