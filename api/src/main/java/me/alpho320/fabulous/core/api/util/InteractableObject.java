package me.alpho320.fabulous.core.api.util;

import java.util.function.Consumer;

public interface InteractableObject<T> {

    T object();
    Consumer<InteractEvent> interactEvent();

    interface InteractEvent {

        boolean interact(InteractEvent event);

    }


}