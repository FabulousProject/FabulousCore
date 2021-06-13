package me.alpho320.fabulous.core.api;

import org.jetbrains.annotations.NotNull;

public interface Callback<T> {

    void whenOpen(@NotNull T object);
    void whenClose(@NotNull T object);

}