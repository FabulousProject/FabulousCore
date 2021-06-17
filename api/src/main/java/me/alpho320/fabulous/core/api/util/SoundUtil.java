package me.alpho320.fabulous.core.api.util;

import java.util.List;

public interface SoundUtil<ENTITY, SOUND> {

    void send(ENTITY entity, SOUND sound);
    void send(ENTITY entity, SOUND sound, float volume);
    void send(ENTITY entity, SOUND sound, float volume, boolean nearby);

    void send(List<ENTITY> entities, SOUND sound);
    void send(List<ENTITY> entities, SOUND sound, float volume);
    void send(List<ENTITY> entities, SOUND sound, float volume, boolean nearby);

}