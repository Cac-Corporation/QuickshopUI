package com.cactt4ck.qsui;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.maxgamer.quickshop.api.QuickShopAPI;
import org.maxgamer.quickshop.shop.Shop;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PShop implements CommandExecutor {

    public static Inventory shopListInventory;
    public static final int NEXT_PAGE_INV_LOCATION = 53, PREVIOUS_PAGE_INV_LOCATION = 45;
    //public static HashMap<ItemStack, Player> players;
    public static HashMap<Player, ItemStack> players;
    public static List<String> playerList;
    private Main main;

    public PShop(Main javaPlugin) {
        this.main = javaPlugin;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
        final Player p = (Player) s;
        if (cmd.getName().equalsIgnoreCase("pshop")) {
            if (args.length > 1)
                return false;
            if (args.length == 0) {
                shopListInventory = Bukkit.createInventory(null, 9 * 6, "§eListe des shops");

                players = new HashMap<Player, ItemStack>();
                playerList = new ArrayList<String>();
                List<String> pShopList = new ArrayList<String>();
                String selectQuery = "select * from shop_location";
                try {
                    Statement selectStatement = main.getConnection().createStatement();
                    ResultSet resultSet = selectStatement.executeQuery(selectQuery);
                    while (resultSet.next())
                        pShopList.add(resultSet.getString("playername"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                int i = 0;
                for (Shop shop : QuickShopAPI.getShopAPI().getAllShops()) {
                    if (!playerList.contains(Bukkit.getOfflinePlayer(shop.getOwner()).getName()) &&
                            pShopList.contains(Bukkit.getOfflinePlayer(shop.getOwner()).getName())) {
                        playerList.add(Bukkit.getOfflinePlayer(shop.getOwner()).getName());
                        shopListInventory.setItem(i, getPlayerHead(shop.getOwner()));
                        i++;
                    }
                }
                p.openInventory(shopListInventory);
                return true;

            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("setwarp")){
                    final Location playerLoc = p.getLocation();
                    String selectQuery = "select * from shop_location where playername=?";
                    try {
                        PreparedStatement selectStatement = main.getConnection().prepareStatement(selectQuery);
                        selectStatement.setString(1, p.getName());
                        ResultSet resultSet = selectStatement.executeQuery();
                        if (resultSet.next()) {
                            String updateQuery = "update shop_location set world=?, x=?, y=?, z=?, yaw=?, pitch=? where playername=?";
                            PreparedStatement updateStatement = main.getConnection().prepareStatement(updateQuery);
                            updateStatement.setString(1, playerLoc.getWorld().getName());
                            updateStatement.setDouble(2, playerLoc.getX());
                            updateStatement.setDouble(3, playerLoc.getY());
                            updateStatement.setDouble(4, playerLoc.getZ());
                            updateStatement.setFloat(5, playerLoc.getYaw());
                            updateStatement.setFloat(6, playerLoc.getPitch());
                            updateStatement.setString(7, p.getName());
                            updateStatement.executeUpdate();
                            p.sendMessage("Le point de téléportation de votre shop a été §l§nmis à jour§r!");
                        } else {
                            String insertQuery = "insert into shop_location values (null, ?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement insertStatement = main.getConnection().prepareStatement(insertQuery);
                            insertStatement.setString(1, p.getName());
                            insertStatement.setString(2, playerLoc.getWorld().getName());
                            insertStatement.setDouble(3, playerLoc.getX());
                            insertStatement.setDouble(4, playerLoc.getY());
                            insertStatement.setDouble(5, playerLoc.getZ());
                            insertStatement.setFloat(6, playerLoc.getYaw());
                            insertStatement.setFloat(7, playerLoc.getPitch());
                            insertStatement.execute();
                            p.sendMessage("Le point de téléportation de votre shop a été §l§ncréé§r!");
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    return true;

                } else if (args[0].equalsIgnoreCase("delwarp")){
                    final Location playerLoc = p.getLocation();
                    String selectQuery = "select * from shop_location where playername=?";
                    try {
                        PreparedStatement selectStatement = main.getConnection().prepareStatement(selectQuery);
                        selectStatement.setString(1, p.getName());
                        ResultSet resultSet = selectStatement.executeQuery();
                        if (resultSet.next()) {
                            String deleteQuery = "delete from shop_location where playername=?";
                            PreparedStatement deleteStatement = main.getConnection().prepareStatement(deleteQuery);
                            deleteStatement.setString(1, p.getName());
                            deleteStatement.execute();
                            p.sendMessage("Le point de téléportation de votre shop a été §l§nsupprimé§r!");
                        } else
                            p.sendMessage("Le point de téléportation de votre shop §l§nn'existe pas§r!");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public static Location getLocationFromConfigYML(String playerName) {
        return Main.config.getLocation("locations." + playerName);
    }

    private ItemStack getPlayerHead(UUID uuid) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        meta.setDisplayName("§fShop de §l§a" + Bukkit.getOfflinePlayer(uuid).getName());
        skull.setItemMeta(meta);
        return skull;
    }

}
