/*
 * MIT License
 *
 * Copyright (c) 2021 Hasan Demirtaş
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.opener;

import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.InventoryContents;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.InventoryOpener;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.SmartInventory;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.holder.SmartInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * an {@link InventoryType#CHEST} implementation for {@link InventoryOpener}.
 */
public final class ChestInventoryOpener implements InventoryOpener {

  @NotNull
  @Override
  public Inventory open(@NotNull final InventoryContents contents) {
    final var page = contents.page();
    if (page.column() != 9) {
      throw new IllegalArgumentException(
        String.format("The column count for the chest inventory must be 9, found: %s.", page.column()));
    }
    if (page.row() < 1 && page.row() > 6) {
      throw new IllegalArgumentException(
        String.format("The row count for the chest inventory must be between 1 and 6, found: %s", page.row()));
    }
    final var holder = new SmartInventoryHolder(contents);
    holder.setActive(true);
    final var handle = Bukkit.createInventory(holder, page.row() * page.column(), page.title());
    SmartInventory.INVENTORIES.put(handle, holder);
    this.fill(handle, contents);
    contents.player().openInventory(handle);
    return handle;
  }

  @Override
  public boolean supports(@NotNull final InventoryType type) {
    return type == InventoryType.CHEST || type == InventoryType.ENDER_CHEST;
  }
}
