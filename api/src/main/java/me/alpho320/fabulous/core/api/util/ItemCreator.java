package me.alpho320.fabulous.core.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> {

    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> name(@NotNull String name);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> type(@NotNull MATERIAL material);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> type(@NotNull String material);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> amount(int amount);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> damage(short damage);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> lore(@NotNull List<String> lore);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> lore(@NotNull String...lore);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> modelData(int modelData);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> enchant(@NotNull ENCHANTMENT enchant);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> enchant(@NotNull ENCHANTMENT enchant, int level);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> enchant(@NotNull List<ENCHANTMENT> enchs);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> enchantFromList(@NotNull List<String> enchs);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> enchant(@NotNull Map<ENCHANTMENT, Integer> enchs);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> flag(@NotNull FLAG flag);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> flag(@NotNull List<FLAG> flags);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> flagFromList(@NotNull List<String> flags);
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> glow();
    @NotNull ItemCreator<ITEM, MATERIAL, ENCHANTMENT, FLAG, PLAYER> glow(boolean state);

    @NotNull Map<ENCHANTMENT, Integer> getEnchantmentsFromList(@NotNull List<String> list);
    @NotNull List<FLAG> getFlagsFromList(@NotNull List<String> list);

    @NotNull ITEM create();
    @NotNull ITEM create(PLAYER player);

}