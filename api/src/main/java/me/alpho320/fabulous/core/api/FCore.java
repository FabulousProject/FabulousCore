package me.alpho320.fabulous.core.api;

import me.alpho320.fabulous.core.api.manager.APIManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FCore<T> {

    boolean init(T initializer);

    T plugin();
    @Nullable APIManager apiManager();
    void setAPIManager(APIManager manager);

    @NotNull String version();

}