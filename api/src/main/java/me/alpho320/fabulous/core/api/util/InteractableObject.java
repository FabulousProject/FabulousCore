package me.alpho320.fabulous.core.api.util;

import java.util.function.Consumer;

public interface InteractableObject<T> {

    T object();
    Consumer<InteractEvent> interactEvent();

    interface InteractEvent {

        Object object();

    }

    interface InteractListener {

        void onInteract(InteractEvent event);

    }

}