package io.puharesource.mc.nuggit.menu;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class Menu implements InventoryHolder {
    private final Inventory inventory;

    public Menu(final int rows) {
        inventory = Bukkit.createInventory(this, rows * 9);
    }

    public Menu(final String name, final int rows) {
        inventory = Bukkit.createInventory(this, rows * 9, name);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
