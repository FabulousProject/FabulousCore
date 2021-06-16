package me.alpho320.fabulous.core.api.manager.impl.sign;

import me.alpho320.fabulous.core.api.IOpenable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public abstract class SignGUI {

    private @NotNull UUID id = UUID.randomUUID();

    private @NotNull String[] lines = new String[]{"", "", "", ""};
    private @NotNull SignType signType = SignType.OAK;

    private final @NotNull SignManager manager;

    private @Nullable IOpenable<String[]> callback = null;
    private @Nullable String channelID;

    public SignGUI(@NotNull SignManager manager) {
        this.manager = manager;
    }

    public @NotNull UUID id() {
        return id;
    }

    public @NotNull String[] lines() {
        return lines;
    }

    public @NotNull SignType type() {
        return signType;
    }

    public @Nullable IOpenable<String[]> callback() {
        return callback;
    }

    public @Nullable String channelID() {
        return channelID;
    }

    public @NotNull SignManager manager() {
        return manager;
    }


    public @NotNull SignGUI setType(@NotNull SignType signType) {
        this.signType = signType;
        return this;
    }

    public @NotNull SignGUI setID(@NotNull UUID id) {
        this.id = id;
        return this;
    }

    public @NotNull SignGUI withLines(String...lines) {
        this.lines = lines;
        return this;
    }

    public @NotNull SignGUI withLines(List<String> lines) {
        return withLines(lines.toArray(new String[]{}));
    }

    public @NotNull SignGUI addLine(String line) {
        return withLines(lines()[lines.length]);
    }

    public @NotNull SignGUI setLine(String line, int index) {
        if (index < 4 && index >= 0) this.lines[index] = line;
        return this;
    }

    public @NotNull SignGUI setCallback(@Nullable IOpenable<String[]> callback) {
        this.callback = callback;
        return this;
    }

    public @NotNull SignGUI setChannelID(String channelID) {
        this.channelID = channelID;
        return this;
    }

    public @NotNull SignGUI open(@NotNull Object player) {
        return open(player, type());
    }
    public @NotNull SignGUI open(@NotNull Object player, @NotNull SignType signType) {
        return open(player, signType, callback());
    }

    public abstract @NotNull SignGUI open(@NotNull Object player, @NotNull SignType signType, @Nullable IOpenable<String[]> callback);

    public enum SignType {
        OAK, ACACIA, BIRCH, SPRUCE, CRIMSON, DARK_OAK, JUNGLE;

        @Nullable
        public static SignType getType(String type) {
            if (type.equalsIgnoreCase("oak"))
                return OAK;
            else if (type.equalsIgnoreCase("acacia"))
                return ACACIA;
            else if (type.equalsIgnoreCase("birch"))
                return BIRCH;
            else if (type.equalsIgnoreCase("spruce"))
                return SPRUCE;
            else if (type.equalsIgnoreCase("crimson"))
                return CRIMSON;
            else if (type.equalsIgnoreCase("dark_oak") || type.equalsIgnoreCase("dark oak"))
                return DARK_OAK;
            else if (type.equalsIgnoreCase("jungle"))
                return JUNGLE;
            else
                return null;
        }

    }

}