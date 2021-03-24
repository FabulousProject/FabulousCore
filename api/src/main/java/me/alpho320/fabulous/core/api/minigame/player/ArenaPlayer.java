package me.alpho320.fabulous.core.api.minigame.player;

import java.util.*;
import org.jetbrains.annotations.*;

public interface ArenaPlayer {

    @NotNull
    UUID getUUID();
    
    @NotNull
    PlayerType getType();
}
