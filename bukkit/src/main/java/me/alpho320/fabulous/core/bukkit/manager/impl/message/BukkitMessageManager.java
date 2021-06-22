package me.alpho320.fabulous.core.bukkit.manager.impl.message;

import me.alpho320.fabulous.core.api.FCore;
import me.alpho320.fabulous.core.api.manager.impl.message.IActionBar;
import me.alpho320.fabulous.core.api.manager.impl.message.MessageManager;
import me.alpho320.fabulous.core.api.manager.impl.message.MessageType;
import me.alpho320.fabulous.core.bukkit.BukkitCore;
import me.alpho320.fabulous.core.bukkit.util.BukkitConfiguration;
import me.alpho320.fabulous.core.bukkit.util.debugger.Debug;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BukkitMessageManager implements MessageManager<CommandSender> {

    private final BukkitCore core;
    private final String prefix;
    private final BukkitConfiguration configuration;

    private IActionBar actionBar;

    public BukkitMessageManager(@NotNull BukkitCore core, @NotNull String prefix, @NotNull BukkitConfiguration configuration) {
        this.core = core;
        this.prefix = prefix;
        this.configuration = configuration;
    }

    @Override
    public boolean setup() {
        Debug.debug(2, "Core " + core);
        Debug.debug(2, "Prefix " + prefix);
        Debug.debug(2, "Configuration " + configuration);

        try {
            PlaceholderAPI.getPlaceholderPattern();
        } catch (Exception e) {
            Debug.debug(1, "BukkitMessageManager - PlaceholderAPI not found!");
            return false;
        }

        try {

            Class<?> clazz = Class.forName("me.alpho320.fabulous.core.nms.bukkit." + core.version() + ".BukkitActionBar");

            if (IActionBar.class.isAssignableFrom(clazz)) {
                this.actionBar = (IActionBar) clazz.getConstructor().newInstance();
                return true;
            }
            Debug.debug(1, core.version() + " is not valid version!");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public @NotNull FCore core() {
        return core;
    }

    @Override
    public @NotNull String colored(@NotNull String message) {
        String text = message;
        if(core.versionInt() >= 16) {

            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                String color = text.substring(matcher.start(), matcher.end());
                text = text.replace(color, ChatColor.of(color) + "");
                matcher = pattern.matcher(text);
            }
        }

        return ChatColor.translateAlternateColorCodes('&', text)
                .replaceAll("%prefix%", prefix);
    }

    @Override
    public @NotNull List<String> colored(@NotNull List<String> list) {
        return list.stream().map(this::colored).collect(Collectors.toList());
    }

    @Override
    public @NotNull String withPlaceholders(@NotNull CommandSender sender, @NotNull String text) {
        return PlaceholderAPI.setPlaceholders((Player) sender, text);
    }

    @Override
    public @NotNull List<String> withPlaceholders(@NotNull CommandSender sender, @NotNull List<String> list) {
        return list.stream().map(text -> withPlaceholders(sender, text)).collect(Collectors.toList());
    }

    @Override
    public @NotNull String coloredWithPlaceholders(@NotNull CommandSender sender, String text) {
        return colored(
                withPlaceholders(
                        sender,
                        text
                )
        );
    }

    @Override
    public @NotNull List<String> coloredWithPlaceholders(@NotNull CommandSender sender, List<String> list) {
        return list.stream().map(text -> colored(withPlaceholders(sender, text))).collect(Collectors.toList());
    }

    @Override
    public void sendMessage(CommandSender sender, String message, MessageType type) {
        if (sender instanceof Player) {
            if (type == MessageType.CHAT) {
                sender.sendMessage(coloredWithPlaceholders(sender, message));
            } else if (type == MessageType.TITLE) {
                ((Player) sender).sendTitle(prefix, coloredWithPlaceholders(sender, message));
            } else if (type == MessageType.ACTIONBAR) {
                actionBar.send(sender, coloredWithPlaceholders(sender, message));
            } else {
                throw new IllegalArgumentException(type + " is not valid a MessageType");
            }
        } else if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(message);
        }
    }

    @Override
    public void sendMessage(CommandSender sender, List<String> messages, MessageType type) {
        messages.forEach(text -> sendMessage(sender, text, type));
    }

    @Override
    public void sendMessage(CommandSender sender, String message, MessageType type, String[] regex, String[] replacement) {
        sendMessage(sender, replace(message, regex, replacement), type);
    }

    @Override
    public void sendMessage(CommandSender sender, List<String> message, MessageType type, String[] regex, String[] replacement) {
        message.forEach(text -> sendMessage(sender, replace(text, regex, replacement), type));
    }

    @Override
    public void sendMessageFromConfig(CommandSender sender, String key, MessageType type) {
        if (configuration.isString("Messages." + key + ".message"))
            sendMessage(sender, colored(configuration.getString("Messages." + key + ".message")), type);
        else if (configuration.isList("Messages." + key + ".message"))
            sendMessage(sender, colored(configuration.getStringList("Messages." + key + ".message")), type);
        else
            throw new IllegalStateException("Configuration of " + configuration + " doesnt have key of " + key);
    }

    @Override
    public void sendMessageFromConfig(CommandSender sender, String key, MessageType type, String[] regex, String[] replacement) {
        if (configuration.isString("Messages." + key + ".message"))
            sendMessage(sender, colored(replace(configuration.getString("Messages." + key + ".message"), regex, replacement)), type);
        else if (configuration.isList("Messages." + key + ".message"))
            sendMessage(sender, colored(replace(configuration.getStringList("Messages." + key + ".message"), regex, replacement)), type);
        else
            throw new IllegalStateException("Configuration of " + configuration + " doesnt have key of " + key);
    }

    @Override
    public void sendTimerMessage(CommandSender sender, String message, MessageType type) {
        sendTimerMessage(sender, message, type, 5);
    }

    @Override
    public void sendTimerMessage(CommandSender sender, String message, MessageType type, int time) {
        sendTimerMessage(sender, message, type, time, 20);
    }

    @Override
    public void sendTimerMessage(CommandSender sender, String message, MessageType type, int time, long period) {
        AtomicInteger i = new AtomicInteger(time);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (i.get() > 0) {
                    sendMessage(sender, message.replaceAll("%time%", String.valueOf(i.get())), type);
                } else {
                    cancel();
                }
                i.getAndDecrement();
            }
        }.runTaskTimerAsynchronously(core.plugin(), 0, period);
    }

    @Override
    public void sendTimerMessage(CommandSender sender, List<String> messages, MessageType type, int time) {
        sendTimerMessage(sender, messages, type, time, 20L);
    }

    @Override
    public void sendTimerMessage(CommandSender sender, List<String> messages, MessageType type, int time, long period) {
        AtomicInteger i = new AtomicInteger(time);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (i.get() > 0) {
                    sendMessage(
                            sender,
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
                i.getAndDecrement();
            }
        }.runTaskTimerAsynchronously(core.plugin(), 0, period);
    }

    @Override
    public void sendTimerMessage(CommandSender sender, String message, MessageType type, String[] regex, String[] replacement, int time) {
        sendTimerMessage(sender, replace(message, regex, replacement), type, time);
    }

    @Override
    public void sendTimerMessage(CommandSender sender, String message, MessageType type, String[] regex, String[] replacement, int time, long period) {
        sendTimerMessage(sender, replace(message, regex, replacement), type, time, period);
    }

    @Override
    public void sendTimerMessage(CommandSender sender, List<String> messages, MessageType type, String[] regex, String[] replacement, int time) {
        sendTimerMessage(sender, replace(messages, regex, replacement), type, time);
    }

    @Override
    public void sendTimerMessage(CommandSender sender, List<String> messages, MessageType type, String[] regex, String[] replacement, int time, long period) {
        sendTimerMessage(sender, replace(messages, regex, replacement), type, time, period);
    }

    @Override
    public void sendTimerMessageFromConfig(CommandSender sender, String key, MessageType type) {
        sendTimerMessageFromConfig(sender, key, type, 5);
    }

    @Override
    public void sendTimerMessageFromConfig(CommandSender sender, String key, MessageType type, int time) {
        sendTimerMessageFromConfig(sender, key, type, time, 20);
    }

    @Override
    public void sendTimerMessageFromConfig(CommandSender sender, String key, MessageType type, int time, long period) {
        if (configuration.isString("Messages." + key + ".message"))
            sendTimerMessage(sender, colored(configuration.getString("Message." + key + ".message")), type, time, period);
        else if (configuration.isList("Messages." + key + ".message"))
            sendTimerMessage(sender, colored(configuration.getStringList("Message." + key + ".message")), type, time, period);
        else
            throw new IllegalStateException("Configuration of " + configuration + " doesnt have key of " + key);
    }
    

    @Override
    public void sendTimerMessageFromConfig(CommandSender sender, String key, MessageType type, String[] regex, String[] replacement, int time) {
        sendTimerMessageFromConfig(sender, key, type, regex, replacement, time, 20);
    }

    @Override
    public void sendTimerMessageFromConfig(CommandSender sender, String key, MessageType type, String[] regex, String[] replacement, int time, long period) {
        if (configuration.isString("Messages." + key + ".message"))
            sendTimerMessage(sender, colored(configuration.getString("Message." + key + ".message")), type, regex, replacement, time, period);
        else if (configuration.isList("Messages." + key + ".message"))
            sendTimerMessage(sender, colored(configuration.getStringList("Message." + key + ".message")), type, regex, replacement, time, period);
        else
            throw new IllegalStateException("Configuration of " + configuration + " doesnt have key of " + key);
    }

}