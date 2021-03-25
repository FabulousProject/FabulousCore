package me.alpho320.fabulous.core.api.manager;

import me.alpho320.fabulous.core.api.manager.impl.interact.InteractableObjManager;
import me.alpho320.fabulous.core.api.manager.impl.message.MessageManager;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignManager;
import me.alpho320.fabulous.core.api.manager.impl.worldborder.WorldBorderManager;
import org.jetbrains.annotations.Nullable;

public interface APIManager {

    boolean init();

    @Nullable MessageManager messageManager();
    @Nullable SignManager signManager();
    @Nullable WorldBorderManager worldBorderManager();
    @Nullable InteractableObjManager interactableObjManager();

}