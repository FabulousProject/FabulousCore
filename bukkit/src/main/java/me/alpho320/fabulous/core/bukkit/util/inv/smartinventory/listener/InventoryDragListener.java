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

import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.SmartHolder;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.event.IcDragEvent;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.util.SlotPos;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

/**
 * a class that represents inventory drag listeners.
 */
public final class InventoryDragListener implements Listener {

  /**
   * listens inventory drag events.
   *
   * @param event the event to listen.
   */
  @EventHandler(priority = EventPriority.LOW)
  public void onInventoryDrag(final InventoryDragEvent event) {
    final var holder = event.getInventory().getHolder();
    if (!(holder instanceof SmartHolder)) {
      return;
    }
    final var smartHolder = (SmartHolder) holder;
    final var inventory = event.getInventory();
    final var contents = smartHolder.getContents();
    for (final var slot : event.getRawSlots()) {
      final var pos = SlotPos.of(slot / 9, slot % 9);
      contents.get(pos).ifPresent(icon ->
        icon.accept(new IcDragEvent(contents, event, icon, smartHolder.getPlugin())));
      if (slot >= inventory.getSize() || contents.isEditable(pos)) {
        continue;
      }
      event.setCancelled(true);
      break;
    }
  }
}
