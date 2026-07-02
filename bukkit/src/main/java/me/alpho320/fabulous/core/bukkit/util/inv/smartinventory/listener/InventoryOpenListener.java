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
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.event.PgOpenEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * a class that represents inventory open listeners.
 */
public final class InventoryOpenListener implements Listener {

  /**
   * listens the inventory open events.
   *
   * @param event the event to listen.
   */
  @EventHandler
  public void onInventoryOpen(final InventoryOpenEvent event) {
    // Resolved via the INVENTORIES cache instead of event.getInventory().getHolder():
    // getHolder() reads block state on block inventories and throws on region-threaded
    // servers (Folia, ShreddedPaper) when the block belongs to another region thread.
    final var smartHolder = SmartInventory.getHolder(event.getPlayer(), event.getInventory()).orElse(null);
    if (smartHolder == null) {
      return;
    }
    smartHolder.getPage().accept(new PgOpenEvent(smartHolder.getContents(), event, smartHolder.getPlugin()));
  }
}
