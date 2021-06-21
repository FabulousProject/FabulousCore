package me.alpho320.fabulous.core.api.manager;

import me.alpho320.fabulous.core.api.manager.impl.cooldown.CooldownManager;
import me.alpho320.fabulous.core.api.manager.impl.interact.InteractableObjManager;
import me.alpho320.fabulous.core.api.manager.impl.message.MessageManager;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignManager;
import me.alpho320.fabulous.core.api.manager.impl.worldborder.WorldBorderManager;
import org.jetbrains.annotations.NotNull;

public interface APIManager {

    boolean init();

    @NotNull CooldownManager cooldownManager();
    @NotNull MessageManager messageManager();
    @NotNull SignManager signManager();
    @NotNull WorldBorderManager worldBorderManager();
    @NotNull InteractableObjManager interactableObjManager();

}