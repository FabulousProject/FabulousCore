package me.alpho320.fabulous.core.api.manager.impl.interact;

import java.util.UUID;
import java.util.function.Consumer;

public abstract class InteractableObject<T> {

    private final UUID id;
    private final T object;

    private final Consumer<InteractEvent> eventConsumer;

    public InteractableObject(UUID id, T object, Consumer<InteractEvent> eventConsumer) {
        this.id = id;
        this.object = object;
        this.eventConsumer = eventConsumer;
    }

    public UUID getId() {
        return id;
    }

    public T getObject() {
        return object;
    }

    public Consumer<InteractEvent> getEventConsumer() {
        return eventConsumer;
    }

    public interface InteractEvent {

        Object object();

    }

}