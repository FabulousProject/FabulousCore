package me.alpho320.fabulous.core.api.manager;

import me.alpho320.fabulous.core.api.FCore;
import org.jetbrains.annotations.NotNull;

public interface IManager {

    boolean setup();
    @NotNull FCore core();

}