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
            for(int i = 0; i <= regex.length - 1; i++) {
                s = s.replaceAll(regex[i], replacement[i]);
            }
            l.add(s);
        }
        return new ArrayList<>(l);
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