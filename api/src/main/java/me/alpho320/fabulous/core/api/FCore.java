package me.alpho320.fabulous.core.api;

import me.alpho320.fabulous.core.api.manager.APIManager;

public interface FCore {

    boolean init(Object initializer);

    APIManager apiManager();

}