package me.alpho320.fabulous.core.bukkit.manager;

import me.alpho320.fabulous.core.api.manager.APIManager;
import me.alpho320.fabulous.core.api.manager.impl.interact.InteractableObjManager;
import me.alpho320.fabulous.core.api.manager.impl.message.MessageManager;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignManager;
import me.alpho320.fabulous.core.api.manager.impl.worldborder.WorldBorderManager;
import org.jetbrains.annotations.Nullable;

public class BukkitAPIManager implements APIManager {

    @Override
    public boolean init(Object plugin) {
        return false;
    }

    @Override
    public @Nullable MessageManager messageManager() {
        return null;
    }

    @Override
    public @Nullable SignManager signManager() {
        return null;
    }

    @Override
    public @Nullable WorldBorderManager worldBorderManager() {
        return null;
    }

    @Override
    public @Nullable InteractableObjManager interactableObjManager() {
        return null;
    }

}