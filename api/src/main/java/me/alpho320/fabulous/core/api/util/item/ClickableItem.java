package me.alpho320.fabulous.core.api.util.item;

import java.util.function.Consumer;

public interface ClickableItem<T> {

    Object item();
    Consumer<ItemClickEvent> clickEvent(); //todo


}