package me.alpho320.fabulous.core.api;

import me.alpho320.fabulous.core.api.manager.APIManager;
import me.alpho320.fabulous.core.api.manager.impl.cooldown.CooldownManager;
import me.alpho320.fabulous.core.api.manager.impl.message.MessageManager;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignManager;
import me.alpho320.fabulous.core.api.manager.impl.worldborder.WorldBorderManager;
import me.alpho320.fabulous.core.api.util.LocationUtil;
import me.alpho320.fabulous.core.api.util.SoundUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FCore<T> {

    boolean init();
    boolean init(@NotNull String prefix);
    boolean init(@NotNull T initializer);
    boolean init(@NotNull T initializer, @NotNull String prefix);

    @NotNull T plugin();
    @NotNull APIManager manager();
    void setAPIManager(@NotNull APIManager manager);

    @NotNull String version();
    int versionInt();

    @NotNull Object getSignMaterial(@Nullable SignGUI.SignType type);

    @NotNull CooldownManager cooldown();
    @NotNull MessageManager message();
    @NotNull SignManager sign();
    @NotNull WorldBorderManager border();
    @NotNull LocationUtil location();
    @NotNull SoundUtil sound();



}