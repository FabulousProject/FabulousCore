package me.alpho320.fabulous.core.api.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface Configuration {

    void saveDefaults(@NotNull String path);
    void reload();
    void save();

    @NotNull File file();

}