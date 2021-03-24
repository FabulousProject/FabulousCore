package me.alpho320.fabulous.core.api.manager.impl.message;

import me.alpho320.fabulous.core.api.manager.IManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface MessageAPI extends IManager {

    @NotNull String colored(@NotNull String text);
    @NotNull List<String> colored(@NotNull List<String> list);
    @NotNull String coloredWithPlaceholders(@NotNull Object player, String text);

    @NotNull String replace(String text, String[] regex, String[] replacement);
    @NotNull List<String> replace(Collection<String> list, String[] regex, String[] replacement);

    void sendMessage(Object sender, String message, MessageType type);
    void sendMessage(Object sender, List<String> messages, MessageType type);
    void sendMessage(Object sender, String message, MessageType type, String[] regex, String[] replacement);
    void sendMessage(Object sender, List<String> message, MessageType type, String[] regex, String[] replacement);

    void sendTimerMessage(Object player, String message, MessageType type);
    void sendTimerMessage(Object player, String message, MessageType type, int time);
    void sendTimerMessage(Object player, List<String> messages, MessageType type, int time);
    void sendTimerMessage(Object player, String message, MessageType type, String[] regex, String[] replacement, int time);
    void sendTimerMessage(Object player, List<String> messages, MessageType type, String[] regex, String[] replacement, int time);

}