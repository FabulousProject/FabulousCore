package me.alpho320.fabulous.core.bukkit.manager;

import me.alpho320.fabulous.core.api.manager.APIManager;
import me.alpho320.fabulous.core.api.manager.impl.cooldown.CooldownManager;
import me.alpho320.fabulous.core.api.manager.impl.interact.InteractableObjManager;
import me.alpho320.fabulous.core.api.manager.impl.message.MessageManager;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignManager;
import me.alpho320.fabulous.core.api.manager.impl.worldborder.WorldBorderManager;
import me.alpho320.fabulous.core.bukkit.BukkitCore;
import me.alpho320.fabulous.core.bukkit.manager.impl.interact.BukkitInteractableObjManager;
import me.alpho320.fabulous.core.bukkit.manager.impl.message.BukkitMessageManager;
import me.alpho320.fabulous.core.bukkit.manager.impl.sign.BukkitSignManager;
import org.jetbrains.annotations.Nullable;

public class BukkitAPIManager implements APIManager {

    private final BukkitCore core;

    private CooldownManager cooldownManager;
    private BukkitInteractableObjManager interactableObjManager;
    private BukkitMessageManager messageManager;
    private BukkitSignManager signManager;

    private String prefix = "";

    public BukkitAPIManager(BukkitCore core) {
        this.core = core;
    }

    public BukkitAPIManager(BukkitCore core, String prefix) {
        this.core = core;
        this.prefix = prefix;
    }

    @Override
    public boolean init() {
        try {

            messageManager = new BukkitMessageManager(core, prefix);
            messageManager.setup();

            cooldownManager = new CooldownManager();
            cooldownManager.setup();

            signManager = new BukkitSignManager(core);
            signManager.setup();

            interactableObjManager = new BukkitInteractableObjManager(this.core);
            interactableObjManager.setup();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public @Nullable CooldownManager cooldownManager() {
        return cooldownManager;
    }

    @Override
    public @Nullable MessageManager messageManager() {
        return messageManager;
    }

    @Override
    public @Nullable SignManager signManager() {
        return signManager;
    }

    @Override
    public @Nullable WorldBorderManager worldBorderManager() {
        return null;
    }

    @Override
    public @Nullable InteractableObjManager interactableObjManager() {
        return interactableObjManager;
    }

}