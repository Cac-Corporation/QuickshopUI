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

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
        final Player p = (Player) s;
        if (cmd.getName().equalsIgnoreCase("pshop")) {
            if (args.length > 0)
                return false;
            shopListInventory = Bukkit.createInventory(null, 9 * 6, "§eListe des shops");

            players = new HashMap<Player, ItemStack>();
            playerList = new ArrayList<String>();
            int i = 0;
            for (Shop shop : QuickShopAPI.getShopAPI().getAllShops()) {
                if (!playerList.contains(Bukkit.getOfflinePlayer(shop.getOwner()).getName()) &&
                        Main.config.getLocation("locations." + Bukkit.getOfflinePlayer(shop.getOwner()).getName()) != null) {
                    playerList.add(Bukkit.getOfflinePlayer(shop.getOwner()).getName());
                    shopListInventory.setItem(i, getPlayerHead(shop.getOwner()));
                    i++;
                }
            }
            p.openInventory(shopListInventory);
            return true;

        } else if (cmd.getName().equalsIgnoreCase("setpshop")) {
            if (args.length > 0)
                return false;
            Main.config.set("locations." + p.getName(), p.getLocation());
            try {
                Main.config.save(Main.configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            p.sendMessage("Point de téléportation de votre shop créé");
            return true;

        } else if (cmd.getName().equalsIgnoreCase("delpshop")) {
            if (args.length > 0)
                return false;
            if (Main.config.getLocation("locations." + p.getName()) == null) {
                p.sendMessage("Vous n'avez pas de point de téléportation à votre shop");
                return true;
            }
            Main.config.set("locations." + p.getName(), "");
            try {
                Main.config.save(Main.configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            p.sendMessage("Point de téléportation de votre shop supprimé");
            return true;
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
