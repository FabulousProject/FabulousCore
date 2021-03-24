package me.alpho320.fabulous.core.api.manager.impl.message;

public enum MessageType {
    CHAT, TITLE, ACTIONBAR;

    public static MessageType getType(String type) {
        if (type.equalsIgnoreCase("chat")) {
            return CHAT;
        } else if (type.equalsIgnoreCase("title")) {
            return TITLE;
        } else if (type.equalsIgnoreCase("actionbar")) {
            return ACTIONBAR;
        } else {
            throw new IllegalArgumentException(type + " is not valid a messageType!");
        }
    }
}
