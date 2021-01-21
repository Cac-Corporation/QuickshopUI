package com.cactt4ck.qsui;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryListeners implements Listener {

    private Main main;

    public InventoryListeners(Main javaPlugin) {
        this.main = javaPlugin;
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGH)
    private void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != PShop.shopListInventory) return;
        if (e.getCurrentItem() == null || e.getCurrentItem().equals(new ItemStack(Material.AIR))) return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        final Player p = (Player) e.getWhoClicked();

        String selectQuery = "select * from shop_location where playername=?";
        try {
            PreparedStatement selectStatement = main.getConnection().prepareStatement(selectQuery);
            selectStatement.setString(1, PShop.playerList.get(e.getSlot()));
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                p.teleport(new Location(
                        Bukkit.getWorld(resultSet.getString("world")),
                        resultSet.getDouble("x"),
                        resultSet.getDouble("y"),
                        resultSet.getDouble("z"),
                        resultSet.getFloat("yaw"),
                        resultSet.getFloat("pitch")
                ));
            }
            p.sendMessage("Vous avez été téléporté au shop de §l§a" + PShop.playerList.get(e.getSlot()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
