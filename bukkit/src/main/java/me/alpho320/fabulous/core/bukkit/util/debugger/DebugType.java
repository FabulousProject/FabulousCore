package me.alpho320.fabulous.core.bukkit.util.debugger;

public enum DebugType {

    INFO(0),
    ERROR(1),
    OPTIONAL(2);


    private final int type;

    DebugType(int i) {
        type = i;
    }

    public static DebugType getType(int i) {
        if (i == 0)
            return INFO;
        else if (i == 1)
            return ERROR;
        else if (i == 2)
            return OPTIONAL;
        else
            throw new IllegalStateException(i + " is not valid type!");
    }

    public int getID() {
        return type;
    }

}