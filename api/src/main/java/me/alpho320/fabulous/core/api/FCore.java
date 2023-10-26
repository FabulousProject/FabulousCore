package me.alpho320.fabulous.core.api;

import me.alpho320.fabulous.core.api.manager.APIManager;
import me.alpho320.fabulous.core.api.manager.impl.cooldown.CooldownManager;
import me.alpho320.fabulous.core.api.manager.impl.message.MessageManager;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignManager;
import me.alpho320.fabulous.core.api.manager.impl.worldborder.WorldBorderManager;
import me.alpho320.fabulous.core.api.util.Configuration;
import me.alpho320.fabulous.core.api.util.LocationUtil;
import me.alpho320.fabulous.core.api.util.SoundUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public interface FCore<T> {

    boolean init();
    boolean init(@NotNull String prefix);
    boolean init(@NotNull T initializer);
    boolean init(@NotNull T initializer, @NotNull String prefix);
    boolean init(@NotNull T initializer, @NotNull String prefix, @NotNull Configuration messageConfiguration);

    @NotNull T plugin();
    @NotNull APIManager manager();
    void setManager(@NotNull APIManager manager);

    @NotNull Configuration messageConfiguration();
    void setConfiguration(@NotNull Configuration configuration);

    @NotNull String version();
    int versionInt();

    @NotNull Object getSignMaterial(@Nullable SignGUI.SignType type);

    @NotNull CooldownManager cooldown();
    @NotNull MessageManager message();
    @NotNull SignManager sign();
    @NotNull WorldBorderManager border();
    @NotNull LocationUtil location();
    @NotNull SoundUtil sound();


    static @Nullable <T> T getField(Object object, Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(object);
            field.setAccessible(false);
            return (value != null) ? (T) value : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}