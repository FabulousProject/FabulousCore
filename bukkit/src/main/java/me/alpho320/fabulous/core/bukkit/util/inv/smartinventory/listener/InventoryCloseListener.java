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

import me.alpho320.fabulous.core.bukkit.BukkitCore;
import me.alpho320.fabulous.core.bukkit.util.debugger.Debug;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.SmartHolder;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.SmartInventory;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.event.PgCloseEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * a class that represents inventory close listeners.
 */
@RequiredArgsConstructor
public final class InventoryCloseListener implements Listener {

  /**
   * the stop tick function.
   */
  @NotNull
  private final Consumer<UUID> stopTickFunction;

  /**
   * listens inventory close events.
   *
   * @param event the event to listen.
   */
  @EventHandler
  public void onInventoryClose(final InventoryCloseEvent event) {
    final var holder = event.getInventory().getHolder();
    Debug.debug(2, "SmartInventoryCloseListener | Inventory: " + event.getInventory() +" | Holder: " + (holder == null ? "null" : holder.getClass().getSimpleName()));
    if (!(holder instanceof SmartHolder)) {
      return;
    }
    final var player = event.getPlayer();
    final var smartHolder = (SmartHolder) holder;
    final var inventory = event.getInventory();
    final var page = smartHolder.getPage();
    final var close = new PgCloseEvent(smartHolder.getContents(), event);
    page.accept(close);

    if (!page.canClose(close)) {
      if (page.async()) {
        BukkitCore.instance().taskScheduler().runTaskAsynchronously(() ->
                player.openInventory(inventory));
      } else {
        BukkitCore.instance().taskScheduler().runTask(() ->
                player.openInventory(inventory));
      }
      return;
    }
    inventory.clear();
    this.stopTickFunction.accept(player.getUniqueId());

    page.inventory().stopTick(player.getUniqueId());
    page.unsubscribeProvider();
    smartHolder.setActive(false);
    SmartHolder remove = SmartInventory.INVENTORIES.remove(inventory);
    SmartHolder removedHolder = SmartInventory.PLAYER_HOLDER.remove(player.getName());
    Debug.debug(2,"SmartInventoryCloseListener | AfterCloseSize: " + SmartInventory.INVENTORIES.size() + " | Removed: " + (remove == null ? "null" : remove.getClass().getSimpleName()) + " | Player: " + player.getName() + " | RemovedHolder: " + (removedHolder == null ? "null" : removedHolder.getClass().getSimpleName()) + " AfterClosePlayerSize: " + SmartInventory.PLAYER_HOLDER.size());
  }
}
