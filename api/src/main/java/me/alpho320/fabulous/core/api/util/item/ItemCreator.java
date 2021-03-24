package me.alpho320.fabulous.core.api.util.item;

import java.util.List;

public interface ItemCreator {

    ItemCreator name(String name);
    ItemCreator amount(int amount);
    ItemCreator damage(short damage);
    ItemCreator lore(List<String> lore);
    ItemCreator lore(String...lore);
    ItemCreator lore(String lore);
    ItemCreator modelData(int modelData);
    ItemCreator enchant(Object enchant);
    ItemCreator enchantments(List<Object> enchs);
    ItemCreator flag(Object flag);
    ItemCreator flags(List<Object> flags);
    ItemCreator type(Object mat);
    ItemCreator glow();

    ItemCreator build();

}