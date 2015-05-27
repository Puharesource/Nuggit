package io.puharesource.mc.nuggit.event.menu;

import io.puharesource.mc.nuggit.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

public abstract class MenuEvent extends PlayerEvent {
    protected final Menu menu;

    public MenuEvent(final Player player, final Menu menu) {
        super(player);
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }
}
