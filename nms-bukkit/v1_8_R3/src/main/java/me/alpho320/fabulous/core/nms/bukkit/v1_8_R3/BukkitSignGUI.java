package me.alpho320.fabulous.core.nms.bukkit.v1_8_R3;

import me.alpho320.fabulous.core.api.manager.impl.interact.InteractableObject;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class BukkitSignGUI implements SignGUI {

    private final UUID id;

    private final SignType signType;
    private final SignCallback callback;

    private String[] lines = new String[]{"", "", "", ""};

    public BukkitSignGUI(UUID id, SignType signType, SignCallback callback) {
        this.id = id;
        this.signType = signType;
        this.callback = callback;
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public boolean open(@NotNull Object player, SignType signType, SignCallback callback) {
        return false;
    }

    @Override
    public String[] lines() {
        return lines;
    }

    @Override
    public SignType signType() {
        return signType;
    }

    @Override
    public SignCallback callback() {
        return callback;
    }

    @Override
    public SignGUI withLines(String... lines) {
        return this;
    }

    @Override
    public SignGUI withLines(List<String> lines) {
        return this;
    }
}