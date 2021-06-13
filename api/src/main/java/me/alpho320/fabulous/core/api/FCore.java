package me.alpho320.fabulous.core.api;

import me.alpho320.fabulous.core.api.manager.APIManager;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FCore<T> {

    boolean init(T initializer);

    T plugin();
    @Nullable APIManager apiManager();
    void setAPIManager(APIManager manager);

    @NotNull String version();
    int versionInt();

    @NotNull Object getSignMaterial(@Nullable SignGUI.SignType type);


}