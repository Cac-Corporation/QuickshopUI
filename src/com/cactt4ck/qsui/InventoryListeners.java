package com.cactt4ck.qsui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListeners implements Listener {

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGH)
    private void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != PShop.shopListInventory) return;
        if (e.getCurrentItem() == null || e.getCurrentItem().equals(new ItemStack(Material.AIR))) return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        final Player p = (Player) e.getWhoClicked();

        p.teleport(PShop.getLocationFromConfigYML(PShop.playerList.get(e.getSlot())));
        p.sendMessage("Vous avez été téléporté au shop de §l§a" + PShop.playerList.get(e.getSlot()));
    }

}
