package me.alpho320.fabulous.core.api.manager.impl.message;

import me.alpho320.fabulous.core.api.manager.IManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface MessageManager<COMMAND_SENDER> extends IManager {

    @NotNull String colored(@NotNull String text);
    @NotNull List<String> colored(@NotNull List<String> list);
    @NotNull String withPlaceholders(@NotNull COMMAND_SENDER sender, @NotNull String text);
    @NotNull List<String> withPlaceholders(@NotNull COMMAND_SENDER sender, @NotNull List<String> list);
    @NotNull String coloredWithPlaceholders(@NotNull COMMAND_SENDER sender, String text);
    @NotNull List<String> coloredWithPlaceholders(@NotNull COMMAND_SENDER sender, List<String> list);

    @NotNull default String replace(String text, String[] regex, String[] replacement) {
        String value = text;
        for(int i = 0; i <= regex.length - 1; i++) {
            value = value.replaceAll(regex[i], replacement[i]);
        }
        return value;
    }
    @NotNull default List<String> replace(Collection<String> list, String[] regex, String[] replacement) {
        Collection<String> l = new ArrayList<>();
        for(String s : list) {
            String copy = s;
            for(int i = 0; i <= regex.length - 1; i++) {
                copy = copy.replaceAll(regex[i], replacement[i]);
            }
            l.add(copy);
        }
        return new ArrayList<>(l);
    }

    @NotNull default String replaceColored(@NotNull String text, @NotNull String[] regex, @NotNull String[] replacement) {
        return colored(replace(text, regex, replacement));
    }

    @NotNull default List<String> replaceColored(@NotNull List<String> list, @NotNull String[] regex, @NotNull String[] replacement) {
        return colored(replace(list, regex, replacement));
    }

    @NotNull default String replaceColoredWithPlaceholders(@NotNull COMMAND_SENDER sender, @NotNull String text, @NotNull String[] regex, @NotNull String[] replacement) {
        return withPlaceholders(sender, colored(replace(text, regex, replacement)));
    }

    @NotNull default List<String> replaceColoredWithPlaceholders(@NotNull COMMAND_SENDER sender, @NotNull List<String> list, @NotNull String[] regex, @NotNull String[] replacement) {
        return withPlaceholders(sender, colored(replace(list, regex, replacement)));
    }

    void sendMessage(COMMAND_SENDER sender, String message, MessageType type);
    void sendMessage(COMMAND_SENDER sender, List<String> messages, MessageType type);
    void sendMessage(COMMAND_SENDER sender, String message, MessageType type, String[] regex, String[] replacement);
    void sendMessage(COMMAND_SENDER sender, List<String> message, MessageType type, String[] regex, String[] replacement);

    void sendMessageFromConfig(COMMAND_SENDER sender, String key, MessageType type);
    void sendMessageFromConfig(COMMAND_SENDER sender, String key, MessageType type, String[] regex, String[] replacement);

    void sendTimerMessage(COMMAND_SENDER sender, String message, MessageType type);
    void sendTimerMessage(COMMAND_SENDER sender, String message, MessageType type, int time);
    void sendTimerMessage(COMMAND_SENDER sender, String message, MessageType type, int time, long period);
    void sendTimerMessage(COMMAND_SENDER sender, List<String> messages, MessageType type, int time);
    void sendTimerMessage(COMMAND_SENDER sender, List<String> messages, MessageType type, int time, long period);
    void sendTimerMessage(COMMAND_SENDER sender, String message, MessageType type, String[] regex, String[] replacement, int time);
    void sendTimerMessage(COMMAND_SENDER sender, String message, MessageType type, String[] regex, String[] replacement, int time, long period);
    void sendTimerMessage(COMMAND_SENDER sender, List<String> messages, MessageType type, String[] regex, String[] replacement, int time);
    void sendTimerMessage(COMMAND_SENDER sender, List<String> messages, MessageType type, String[] regex, String[] replacement, int time, long period);

    void sendTimerMessageFromConfig(COMMAND_SENDER sender, String key, MessageType type);
    void sendTimerMessageFromConfig(COMMAND_SENDER sender, String key, MessageType type, int time);
    void sendTimerMessageFromConfig(COMMAND_SENDER sender, String key, MessageType type, int time, long period);
    void sendTimerMessageFromConfig(COMMAND_SENDER sender, String key, MessageType type, String[] regex, String[] replacement, int time);
    void sendTimerMessageFromConfig(COMMAND_SENDER sender, String key, MessageType type, String[] regex, String[] replacement, int time, long period);

}