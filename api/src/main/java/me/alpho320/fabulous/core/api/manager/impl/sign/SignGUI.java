package me.alpho320.fabulous.core.api.manager.impl.sign;

import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI.SignType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface SignGUI {

    UUID id();

    boolean open(@NotNull Object player, SignType signType, SignCallback callback);

    String[] lines();
    SignType signType();
    SignCallback callback();

    SignGUI withLines(String...lines);
    SignGUI withLines(List<String> lines);

    interface SignCallback {

        void whenOpened(String[] lines);
        void whenClosed(String[] lines);

    }

    enum SignType {
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