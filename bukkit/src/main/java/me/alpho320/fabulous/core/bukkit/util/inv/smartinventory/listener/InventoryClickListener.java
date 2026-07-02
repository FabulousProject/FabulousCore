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

package me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.listener;

import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.SmartInventory;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.event.IcClickEvent;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.event.PgBottomClickEvent;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.event.PgClickEvent;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.event.PgOutsideClickEvent;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.util.SlotPos;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * a class that represents inventory click listeners.
 */
public final class InventoryClickListener implements Listener {

  /**
   * listens inventory click events.
   *
   * @param event the event to listen.
   */
  @EventHandler
  public void onInventoryClick(final InventoryClickEvent event) {
    // Resolved via the INVENTORIES cache instead of event.getInventory().getHolder():
    // getHolder() reads block state on block inventories and throws on region-threaded
    // servers (Folia, ShreddedPaper) when the block belongs to another region thread.
    final var smartHolder = SmartInventory.getHolder(event.getWhoClicked(), event.getInventory()).orElse(null);
    if (smartHolder == null) {
      return;
    }
    // Respect external cancellation. If an earlier listener already cancelled this click
    // (e.g. to block GUI interaction during a sensitive op such as a cross-server transfer),
    // bail out BEFORE dispatching any icon/page handler below. The icon click handler runs a
    // button's action (depo claims, withdrawals, …); letting it fire on an already-cancelled
    // click means "cancelling the click" does not actually block the action — an abusable dupe
    // window. Placed before the framework's own setCancelled(...) calls so it only catches
    // cancels made by other listeners, never the framework's own non-editable/collect guards.
    if (event.isCancelled()) {
      return;
    }
    if (event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
      event.setCancelled(true);
      return;
    }
    final var page = smartHolder.getPage();
    final var contents = smartHolder.getContents();
    final var plugin = smartHolder.getPlugin();
    final var clicked = event.getClickedInventory();
    if (clicked == null) {
      page.accept(new PgOutsideClickEvent(contents, event, plugin));
      return;
    }
    final var player = event.getWhoClicked();
    if (clicked.equals(player.getOpenInventory().getBottomInventory())) {
      page.accept(new PgBottomClickEvent(contents, event, plugin));
      return;
    }
    final var current = event.getCurrentItem();
    if (current == null || current.getType() == Material.AIR) {
      page.accept(new PgClickEvent(contents, event, plugin));
      return;
    }
    final var slot = event.getSlot();
    final var row = slot / 9;
    final var column = slot % 9;
    if (!page.checkBounds(row, column)) {
      return;
    }
    final var slotPos = SlotPos.of(row, column);
    if (!contents.isEditable(slotPos)) {
      event.setCancelled(true);
    }
    contents.get(slotPos).ifPresent(item ->
      item.accept(new IcClickEvent(contents, event, item, plugin)));
    if (!contents.isEditable(slotPos) && player instanceof Player) {
      ((Player) player).updateInventory();
    }
  }
}
