package me.alpho320.fabulous.core.api.manager.impl.message;

import me.alpho320.fabulous.core.api.manager.IManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public interface MessageManager extends IManager {

    @NotNull String colored(@NotNull String text);
    @NotNull List<String> colored(@NotNull List<String> list);
    @NotNull String withPlaceholders(@NotNull Object player, @NotNull String text);
    @NotNull List<String> withPlaceholders(@NotNull Object player, @NotNull List<String> list);
    @NotNull String coloredWithPlaceholders(@NotNull Object player, String text);
    @NotNull List<String> coloredWithPlaceholders(@NotNull Object player, List<String> list);

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

    void sendMessage(Object sender, String message, MessageType type);
    void sendMessage(Object sender, List<String> messages, MessageType type);
    void sendMessage(Object sender, String message, MessageType type, String[] regex, String[] replacement);
    void sendMessage(Object sender, List<String> message, MessageType type, String[] regex, String[] replacement);

    void sendTimerMessage(Object player, String message, MessageType type);
    void sendTimerMessage(Object player, String message, MessageType type, int time);
    void sendTimerMessage(Object player, String message, MessageType type, int time, long period);
    void sendTimerMessage(Object player, List<String> messages, MessageType type, int time);
    void sendTimerMessage(Object player, List<String> messages, MessageType type, int time, long period);
    void sendTimerMessage(Object player, String message, MessageType type, String[] regex, String[] replacement, int time);
    void sendTimerMessage(Object player, String message, MessageType type, String[] regex, String[] replacement, int time, long period);
    void sendTimerMessage(Object player, List<String> messages, MessageType type, String[] regex, String[] replacement, int time);
    void sendTimerMessage(Object player, List<String> messages, MessageType type, String[] regex, String[] replacement, int time, long period);

}