package io.puharesource.mc.nuggit.backend.listeners;

import io.puharesource.mc.nuggit.event.MenuClickEvent;
import io.puharesource.mc.nuggit.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public final class ListenerInventory implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryEvent(InventoryEvent event) {
        if (event.getInventory().getHolder() instanceof Menu &&
                event instanceof Cancellable &&
                !(event instanceof InventoryCloseEvent || event instanceof InventoryOpenEvent)) {

            ((Cancellable) event).setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();

        if (!(event.getWhoClicked() instanceof Player)) return;
        if (inventory.getHolder() == null || !(inventory.getHolder() instanceof Menu)) return;
        final Menu menu = (Menu) inventory.getHolder();
        final int slot = event.getRawSlot();
        if (slot >= menu.getInventory().getSize()) return;

        final MenuClickEvent menuEvent = new MenuClickEvent(((Player) event.getWhoClicked()), menu, event.getRawSlot());
        Bukkit.getPluginManager().callEvent(menuEvent);

        if (!menuEvent.isCancelled()) {
            //TODO: Do something, if I decide to add methods of making easy clickable items.
        }
    }
}
