package io.puharesource.mc.nuggit.event;

import io.puharesource.mc.nuggit.event.menu.MenuEvent;
import io.puharesource.mc.nuggit.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class MenuClickEvent extends MenuEvent implements Cancellable {
    private final int slot;
    private boolean cancel;

    public MenuClickEvent(final Player player, final Menu menu, final int slot) {
        super(player, menu);
        this.slot = slot;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getSlot() {
        return slot;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
