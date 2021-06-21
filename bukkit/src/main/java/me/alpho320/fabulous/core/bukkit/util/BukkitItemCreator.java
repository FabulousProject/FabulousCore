package me.alpho320.fabulous.core.bukkit.util;

import me.alpho320.fabulous.core.api.util.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"rawtypes", "unchecked"})
public class BukkitItemCreator implements ItemCreator<ItemStack, Material, Enchantment, ItemFlag> {

    private @NotNull String name = "null";
    private @NotNull String material = "AIR";
    private @NotNull List<String> lore = new ArrayList<>();
    private @NotNull Map<Enchantment, Integer> enchantments = new HashMap<>();
    private @NotNull List<ItemFlag> flags = new ArrayList<>();

    private boolean glow = false;
    private int amount = 1;
    private short damage = -1;
    private int modelData = -1;

    @Override
    public @NotNull ItemCreator name(@NotNull String name) {
        this.name = name;
        return this;
    }

    @Override
    public @NotNull ItemCreator type(@NotNull Material material) {
        this.material = material.toString();
        return this;
    }

    @Override
    public @NotNull ItemCreator type(@NotNull String material) {
        this.material = material;
        return this;
    }

    @Override
    public @NotNull ItemCreator amount(int amount) {
        this.amount = Math.max(1, amount);
        return this;
    }

    @Override
    public @NotNull ItemCreator damage(short damage) {
        this.damage = (short) Math.max(-1, damage);
        return this;
    }

    @Override
    public @NotNull ItemCreator lore(@NotNull List<String> lore) {
        this.lore = lore;
        return this;
    }

    @Override
    public @NotNull ItemCreator lore(@NotNull String...lore) {
        this.lore = Arrays.asList(lore);
        return this;
    }

    @Override
    public @NotNull ItemCreator modelData(int modelData) {
        this.modelData = modelData;
        return this;
    }

    @Override
    public @NotNull ItemCreator enchant(@NotNull Enchantment enchant) {
        return enchant(enchant, 1);
    }

    @Override
    public @NotNull ItemCreator enchant(@NotNull Enchantment enchant, int level) {
        enchantments.put(enchant, level);
        return this;
    }

    @Override
    public @NotNull ItemCreator enchant(@NotNull List<Enchantment> enchs) {
        enchs.forEach(this::enchant);
        return this;
    }

    @Override
    public @NotNull ItemCreator enchantFromList(@NotNull List<String> enchs) {
        getEnchantmentsFromList(enchs).forEach(this::enchant);
        return this;
    }


    @Override
    public @NotNull ItemCreator enchant(@NotNull Map<Enchantment, Integer> enchs) {
        this.enchantments = enchs;
        return this;
    }

    @Override
    public @NotNull ItemCreator flag(@NotNull ItemFlag flag) {
        this.flags.add(flag);
        return this;
    }

    @Override
    public @NotNull ItemCreator flag(@NotNull List<ItemFlag> itemFlags) {
        this.flags.addAll(itemFlags);
        return this;
    }

    /**
       @see BukkitItemCreator#getFlagsFromList(List);
     */
    @Override
    public @NotNull ItemCreator flagFromList(@NotNull List<String> flags) {
        return flag(getFlagsFromList(flags));
    }

    @Override
    public @NotNull ItemCreator glow() {
        return glow(true);
    }

    @Override
    public @NotNull ItemCreator glow(boolean state) {
        this.glow = state;
        return this;
    }


    @Override
    public @NotNull ItemStack create() {
        ItemStack item = material.length() >= 30 ? itemFromBase64(material) : new ItemStack(Material.matchMaterial(material));

        item.setAmount(amount);
        if (damage > 0)
            item.setDurability(damage);
        if (enchantments.size() > 0)
            item.addUnsafeEnchantments(enchantments);

        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();

            if (!name.equals("null"))
                meta.setDisplayName(name);
            if (lore.size() > 0)
                meta.setLore(lore);
            if (flags.size() > 0)
                flags.forEach(meta::addItemFlags);
            if (modelData > 0)
                meta.setCustomModelData(modelData);
            if (glow) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            }

            item.setItemMeta(meta);
        }

        return item;
    }


    /**
     * Format: ENCHANTMENT_NAME:LEVEL
     *             UNBREAKING:2
     * @throws IllegalArgumentException if format is invalid.
     */
    @Override
    public @NotNull Map<Enchantment, Integer> getEnchantmentsFromList(@NotNull List<String> list) {
        Map<Enchantment, Integer> map = new HashMap<>();

        for (String enchantment : list) {
            try {
                String[] split = enchantment.split(":");

                if (Enchantment.getByName(split[0]) == null) throw new IllegalArgumentException(split[0] + " is not valid a enchantment!");

                map.put(Enchantment.getByName(split[0]), Integer.parseInt(split[1]));
            } catch (Exception e) {
                throw new IllegalArgumentException(enchantment + " is not valid type of ench!");
            }
        }

        return map;
    }

    /**
     * @throws IllegalArgumentException if format is invalid.
     */
    @Override
    public @NotNull List<ItemFlag> getFlagsFromList(@NotNull List<String> list) {
        List<ItemFlag> flags = new ArrayList<>();

        for (String flag : list) {
            try {
                flags.add(ItemFlag.valueOf(flag));
            } catch (Exception e) {
                throw new IllegalArgumentException(flag + " is not valid ItemFlag!");
            }
        }
        return flags;
    }


    /**
     * Creates a player skull based on a base64 string containing the link to the skin.
     *
     * @param base64 The base64 string containing the texture
     * @return The head with a custom texture
     */
    private ItemStack itemFromBase64(String base64) {
        ItemStack item = getHeadItem();
        return itemWithBase64(item, base64);
    }

    /**
     * Applies the base64 string to the ItemStack.
     *
     * @param item The ItemStack to put the base64 onto
     * @param base64 The base64 string containing the texture
     * @return The head with a custom texture
     */
    private ItemStack itemWithBase64(ItemStack item, String base64) {
        UUID hashAsId = new UUID(base64.hashCode(), base64.hashCode());
        return Bukkit.getUnsafe().modifyItemStack(item,
                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}"
        );
    }

    private boolean newerApi() {
        try {

            Material.valueOf("PLAYER_HEAD");
            return true;

        } catch (IllegalArgumentException e) { // If PLAYER_HEAD doesn't exist
            return false;
        }
    }

    private ItemStack getHeadItem() {
        if (newerApi()) {
            return new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } else {
            return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (byte) 3);
        }
    }

}