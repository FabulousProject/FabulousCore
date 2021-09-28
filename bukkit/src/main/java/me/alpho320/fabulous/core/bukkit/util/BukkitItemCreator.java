package me.alpho320.fabulous.core.bukkit.util;

import me.alpho320.fabulous.core.api.util.ItemCreator;
import me.alpho320.fabulous.core.bukkit.BukkitCore;
import me.alpho320.fabulous.core.bukkit.util.debugger.Debug;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BukkitItemCreator implements ItemCreator<ItemStack, Material, Enchantment, ItemFlag, Player> {

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
    public @NotNull BukkitItemCreator name(@NotNull String name) {
        this.name = name;
        return this;
    }

    @Override
    public @NotNull BukkitItemCreator type(@NotNull Material material) {
        this.material = material.toString();
        return this;
    }

    @Override
    public @NotNull BukkitItemCreator type(@NotNull String material) {
        this.material = material;
        return this;
    }

    @Override
    public @NotNull BukkitItemCreator amount(int amount) {
        this.amount = Math.max(1, amount);
        return this;
    }

    @Override
    public @NotNull BukkitItemCreator damage(short damage) {
        this.damage = (short) Math.max(-1, damage);
        return this;
    }

    @Override
    public @NotNull BukkitItemCreator lore(@NotNull List<String> lore) {
        this.lore = lore;
        return this;
    }

    @Override
    public @NotNull BukkitItemCreator lore(@NotNull String...lore) {
        this.lore = Arrays.asList(lore);
        return this;
    }

    @Override
    public @NotNull BukkitItemCreator modelData(int modelData) {
        this.modelData = modelData;
        return this;
    }

    @Override
    public @NotNull BukkitItemCreator enchant(@NotNull Enchantment enchant) {
        return enchant(enchant, 1);
    }

    @Override
    public @NotNull BukkitItemCreator enchant(@NotNull Enchantment enchant, int level) {
        enchantments.put(enchant, level);
        return this;
    }

    @Override
    public @NotNull BukkitItemCreator enchant(@NotNull List<Enchantment> enchs) {
        enchs.forEach(this::enchant);
        return this;
    }

    @Override
    public @NotNull BukkitItemCreator enchantFromList(@NotNull List<String> enchs) {
        getEnchantmentsFromList(enchs).forEach(this::enchant);
        return this;
    }


    @Override
    public @NotNull BukkitItemCreator enchant(@NotNull Map<Enchantment, Integer> enchs) {
        this.enchantments = enchs;
        return this;
    }

    @Override
    public @NotNull BukkitItemCreator flag(@NotNull ItemFlag flag) {
        this.flags.add(flag);
        return this;
    }

    @Override
    public @NotNull BukkitItemCreator flag(@NotNull List<ItemFlag> itemFlags) {
        this.flags.addAll(itemFlags);
        return this;
    }

    /**
       @see BukkitItemCreator#getFlagsFromList(List);
     */
    @Override
    public @NotNull BukkitItemCreator flagFromList(@NotNull List<String> flags) {
        return flag(getFlagsFromList(flags));
    }

    @Override
    public @NotNull BukkitItemCreator glow() {
        return glow(true);
    }

    @Override
    public @NotNull BukkitItemCreator glow(boolean state) {
        this.glow = state;
        return this;
    }


    @Override
    public @NotNull ItemStack create() {
        return create(null);
    }

    @Override
    public @NotNull ItemStack create(Player player) {
        ItemStack item;

        if (material.length() >= 30) {
            item = itemFromBase64(material); // if length is greater than 30, thats mean material is a custom head. (example: https://minecraft-heads.com/custom-heads/)
        } else if (player != null) {
            if (material.startsWith("head_"))
                item = skullFromName(material.split("_")[1]);
            else if (material.equalsIgnoreCase("%player_head%"))
                item = skullFromName(player.getName());
            else
                item = new ItemStack(Material.matchMaterial(material));
        // [SPLASH]_POTION:SPEED:2
        } else if (material.contains("POTION") || material.contains("potion")) {
            String[] split = material.split(":");
            item = new Potion(PotionType.valueOf(split[1]), Integer.parseInt(split[2]), material.contains("SPLASH")).toItemStack(amount);
        } else {
            item = new ItemStack(Material.matchMaterial(material));
        }
        ItemMeta meta = item.getItemMeta();

        item.setAmount(amount);

        if (damage > 0) item.setDurability(damage);
        if (enchantments.size() > 0) item.addUnsafeEnchantments(enchantments);

        Debug.debug(2, "item1 " + item);
        Debug.debug(2, "hasMeta " + item.hasItemMeta());

        if (!name.equals("null")) {
            Debug.debug(2, "name " + name);
            meta.setDisplayName(name);
        }
        if (lore.size() > 0) {
            Debug.debug(2, "lore " + lore);
            meta.setLore(lore);
        }

        if (flags.size() > 0) flags.forEach(meta::addItemFlags);
        if (modelData > 0) meta.setCustomModelData(modelData);

        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        item.setItemMeta(meta);

        Debug.debug(2, "last item " + item);
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
     * Creates a player skull based on a player's name.
     *
     * @param name The Player's name
     * @return The head of the Player
     *
     * @deprecated names don't make for good identifiers
     */
    @Deprecated
    public ItemStack skullFromName(String name) {
        ItemStack item = getHeadItem();

        return itemWithName(item, name);
    }

    /**
     * Creates a player skull based on a player's name.
     *
     * @param item The item to apply the name to
     * @param name The Player's name
     * @return The head of the Player
     *
     * @deprecated names don't make for good identifiers
     */
    @Deprecated
    public ItemStack itemWithName(ItemStack item, String name) {
        return Bukkit.getUnsafe().modifyItemStack(item,
                "{SkullOwner:\"" + name + "\"}"
        );
    }

    /**
     * Creates a player skull based on a base64 string containing the link to the skin.
     *
     * @param base64 The base64 string containing the texture
     * @return The head with a custom texture
     */
    public ItemStack itemFromBase64(String base64) {
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
    public ItemStack itemWithBase64(ItemStack item, String base64) {
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

    public ItemStack getHeadItem() {
        if (newerApi()) {
            return new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } else {
            return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (byte) 3);
        }
    }

}