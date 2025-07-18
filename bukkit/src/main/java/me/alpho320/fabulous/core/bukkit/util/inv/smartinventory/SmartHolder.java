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

package me.alpho320.fabulous.core.bukkit.util.inv.smartinventory;

import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.InventoryContents;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.Page;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * an interface to determine inventory holders.
 */
public interface SmartHolder extends InventoryHolder {

  /**
   * obtains the contents.
   *
   * @return contents.
   */
  @NotNull
  InventoryContents getContents();

  /**
   * obtains the page.
   *
   * @return page.
   */
  @NotNull
  Page getPage();

  /**
   * obtains the player.
   *
   * @return player.
   */
  @NotNull
  Player getPlayer();

  /**
   * obtains the plugin.
   *
   * @return plugin.
   */
  @NotNull
  Plugin getPlugin();

  /**
   * checks if the holder is active.
   *
   * @return {@code true} if the holder is active.
   */
  boolean isActive();

  /**
   * sets the active.
   *
   * @param active the active to set.
   */
  void setActive(boolean active);
}
