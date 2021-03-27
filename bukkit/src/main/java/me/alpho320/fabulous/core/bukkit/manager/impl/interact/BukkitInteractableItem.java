package me.alpho320.fabulous.core.bukkit.manager.impl.interact;

import me.alpho320.fabulous.core.api.manager.impl.interact.InteractableObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public class BukkitInteractableItem extends InteractableObject<ItemStack> implements Listener {

    public BukkitInteractableItem(UUID id, ItemStack item, Consumer<InteractEvent> interactConsumer) {
        super(id, item, interactConsumer);
    }

    @NotNull
    public BukkitInteractableItem registerListener(@NotNull final Plugin plugin){
        plugin.getServer().getPluginManager().registerEvents(new ItemListener(this), plugin);
        return this;
    }

    public class ItemListener implements Listener {

        private final BukkitInteractableItem item;

        public ItemListener(@NotNull final BukkitInteractableItem item){
            this.item = item;
        }

        @EventHandler
        public void playerInteract(final PlayerInteractEvent event){
            final Player player = event.getPlayer();

            final Action action = event.getAction();

            if (this.item.getObject().isSimilar(event.getItem())){
                event.setCancelled(true);

                final ItemClickEvent itemClick = new ItemClickEvent(player, event.getItem());

                if (action.name().startsWith("RIGHT_")){
                    itemClick.setClickType(ClickType.RIGHT);
                }else itemClick.setClickType(ClickType.LEFT);

                Bukkit.getPluginManager().callEvent(itemClick);
            }
        }

        @EventHandler
        public void itemClick(final ItemClickEvent event){
            getEventConsumer().accept(event);
        }

    }

    public abstract static class EventBase extends Event implements Cancellable {

        private static final HandlerList handlers;

        static{
            handlers = new HandlerList();
        }

        private final Player player;

        private boolean cancellable;

        public EventBase(@NotNull final Player player){
            this.player = player;
        }

        @NotNull
        public Player getPlayer() {
            return player;
        }

        @Override
        public boolean isCancelled() {
            return this.cancellable;
        }

        @Override
        public void setCancelled(@NotNull final boolean cancel) {
            this.cancellable = cancel;
        }

        public static HandlerList getHandlerList(){
            return handlers;
        }

        @Override
        public @NotNull HandlerList getHandlers() {
            return handlers;
        }
    }

    public static class ItemClickEvent extends EventBase implements InteractEvent {

        private final ItemStack item;

        private ClickType clickType;

        public ItemClickEvent(@NotNull Player player, ItemStack item) {
            super(player);
            this.item = item;
        }

        @Override
        public Object object() {
            return item;
        }

        public ClickType getClickType() {
            return clickType;
        }

        public void setClickType(ClickType clickType) {
            this.clickType = clickType;
        }
    }

    public enum ClickType{
        RIGHT,
        LEFT;
    }

}