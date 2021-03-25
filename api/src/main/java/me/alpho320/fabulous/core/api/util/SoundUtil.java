package me.alpho320.fabulous.core.api.util;

import java.util.List;

public interface SoundUtil {

    boolean send(Object entity, Object sound);
    boolean send(Object entity, Object sound, float volume);
    boolean send(List<Object> entity, Object sound);
    boolean send(List<Object> entity, Object sound, float volume);

}