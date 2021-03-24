package me.alpho320.fabulous.core.api.manager.impl.sign;

import me.alpho320.fabulous.core.api.manager.IManager;

import java.util.Map;
import java.util.UUID;

public interface SignManager extends IManager {

    Map<UUID, SignGUI> guiMap();

}