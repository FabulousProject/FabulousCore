package me.alpho320.fabulous.core.api.util;

import java.util.List;

public interface ItemCreator<T> {

    ItemCreator<T> name(String name);
    ItemCreator<T> amount(int amount);
    ItemCreator<T> damage(short damage);
    ItemCreator<T> lore(List<String> lore);
    ItemCreator<T> lore(String...lore);
    ItemCreator<T> lore(String lore);
    ItemCreator<T> modelData(int modelData);
    ItemCreator<T> enchant(Object enchant);
    ItemCreator<T> enchantments(List<Object> enchs);
    ItemCreator<T> flag(Object flag);
    ItemCreator<T> flags(List<Object> flags);
    ItemCreator<T> type(Object mat);
    ItemCreator<T> glow();

    T create();

}