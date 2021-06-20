package me.alpho320.fabulous.core.api;

import me.alpho320.fabulous.core.api.manager.APIManager;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI;
import me.alpho320.fabulous.core.api.util.SerializedLocation;
import me.alpho320.fabulous.core.api.util.SoundUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FCore<T> {

    boolean init();
    boolean init(T initializer);

    @NotNull T plugin();
    @Nullable APIManager apiManager();
    void setAPIManager(APIManager manager);

    @NotNull String version();
    int versionInt();

    @NotNull Object getSignMaterial(@Nullable SignGUI.SignType type);
    @NotNull SerializedLocation location();
    @NotNull SoundUtil sound();

}