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

package me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.manager;

import com.github.Anon8281.universalScheduler.UniversalRunnable;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.InventoryOpener;
import me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.SmartInventory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * an implementation for {@link SmartInventory}.
 */
@Getter
@RequiredArgsConstructor
public final class BasicSmartInventory implements SmartInventory {

  /**
   * the openers.
   */
  private final Collection<InventoryOpener> openers = new ArrayList<>();

  /**
   * the plugin.
   */
  @NotNull
  private final Plugin plugin;

  /**
   * the tasks.
   */
  private final Map<UUID, UniversalRunnable> tasks = new ConcurrentHashMap<>();

  static {
    try {
      Class.forName("me.alpho320.fabulous.core.bukkit.util.inv.smartinventory.event.PlgnDisableEvent");
    } catch (final ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
