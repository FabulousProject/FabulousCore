package me.alpho320.fabulous.core.api.manager;

import me.alpho320.fabulous.core.api.manager.impl.message.MessageAPI;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignAPI;

public interface APIManager {

    MessageAPI messageAPI();
    SignAPI signAPI();

}