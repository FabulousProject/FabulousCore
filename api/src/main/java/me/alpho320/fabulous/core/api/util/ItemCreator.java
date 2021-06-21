package me.alpho320.fabulous.core.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> {

    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> name(@NotNull String name);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> type(@NotNull MATERIAL material);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> type(@NotNull String material);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> amount(int amount);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> damage(short damage);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> lore(@NotNull List<String> lore);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> lore(@NotNull String...lore);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> modelData(int modelData);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> enchant(@NotNull ENCHANTMENT enchant);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> enchant(@NotNull ENCHANTMENT enchant, int level);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> enchant(@NotNull List<ENCHANTMENT> enchs);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> enchantFromList(@NotNull List<String> enchs);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> enchant(@NotNull Map<ENCHANTMENT, Integer> enchs);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> flag(@NotNull FLAG flag);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> flag(@NotNull List<FLAG> flags);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> flagFromList(@NotNull List<String> flags);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> glow();
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG> glow(boolean state);

    @NotNull Map<ENCHANTMENT, Integer> getEnchantmentsFromList(@NotNull List<String> list);
    @NotNull List<FLAG> getFlagsFromList(@NotNull List<String> list);

    @NotNull ITEM create();

}