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

package me.alpho320.fabulous.core.bukkit.util.inv.observer;

import me.alpho320.fabulous.core.bukkit.util.inv.observer.Target;
import org.jetbrains.annotations.NotNull;

/**
 * a class that is the observer's source.
 *
 * @param <T> type of the arguments.
 */
public interface Source<T> {

  /**
   * notifies {@link me.alpho320.fabulous.core.bukkit.util.inv.observer.Target#update(Object)} method all of the subscribes.
   *
   * @param argument the argument to notify.
   */
  void notifyTargets(@NotNull T argument);

  /**
   * subscribes the given {@link me.alpho320.fabulous.core.bukkit.util.inv.observer.Target} into the source.
   *
   * @param target the target to subscribe.
   */
  void subscribe(@NotNull me.alpho320.fabulous.core.bukkit.util.inv.observer.Target<T> target);

  /**
   * remove the given {@link me.alpho320.fabulous.core.bukkit.util.inv.observer.Target} from the subscriptions of the source.
   *
   * @param target the target to remove.
   */
  void unsubscribe(@NotNull Target<T> target);
}
