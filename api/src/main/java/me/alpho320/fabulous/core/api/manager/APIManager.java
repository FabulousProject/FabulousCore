package me.alpho320.fabulous.core.api.manager;

import me.alpho320.fabulous.core.api.manager.impl.message.MessageManager;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignManager;

public interface APIManager {

    MessageManager messageAPI();
    SignManager signAPI();

}