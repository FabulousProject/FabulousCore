package me.alpho320.fabulous.core.api.manager.impl.interact;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public interface InteractableObject<T> {

    @NotNull UUID id();

    @NotNull T object();
    @NotNull Consumer<InteractEvent> interactEvent();

    interface InteractEvent {

        Object object();

    }

    interface InteractListener {

        void onInteract(InteractEvent event);

    }

}