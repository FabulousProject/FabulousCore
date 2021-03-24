package me.alpho320.fabulous.core.api.manager;

import me.alpho320.fabulous.core.api.manager.impl.message.MessageManager;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignManager;
import me.alpho320.fabulous.core.api.manager.impl.worldborder.WorldBorderManager;

public interface APIManager {

    boolean init(Object plugin);

    MessageManager messageManager();
    SignManager signManager();
    WorldBorderManager worldBorderManager();

}