package io.puharesource.mc.nuggit.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class ItemBuilder {

    private ItemStack item;

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder(Material material) {
        item = new ItemStack(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        item = new ItemStack(material, amount);
    }

    public ItemBuilder(Material material, int amount, byte data) {
        item = new ItemStack(material, amount, data);
    }

    public void addLoreline(String line) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        lore.add(line);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public void setLore(List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public void setAmount(int amount) {
        item.setAmount(amount);
    }

    public void setName(String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

    public void addEnchantment(Enchantment enchantment, int level) {
        item.addEnchantment(enchantment, level);
    }

    public void addUnsafeEnchantment(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
    }

    public void addItemFlags(ItemFlag... itemFlags) {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(itemFlags);
        item.setItemMeta(meta);
    }

    public boolean isRepairable() {
        return item != null && !(item.getType().getMaxDurability() < 1 || item.getType().isBlock() || item.getDurability() == 0);
    }

    public void repairItem() {
        if (!isRepairable()) return;
        item.setDurability((short) 0);
    }

    public boolean isUnbreakable() {
        return item.getItemMeta().spigot().isUnbreakable();
    }

    public void setUnbreakable(boolean unbreakable) {
        item.getItemMeta().spigot().setUnbreakable(unbreakable);
    }

    public void damage(int damage) {
        item.setDurability((short) (damage + item.getDurability()));
    }

    public ItemStack getItem() {
        return item;
    }
}
