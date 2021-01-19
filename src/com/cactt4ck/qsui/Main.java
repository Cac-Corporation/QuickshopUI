package com.cactt4ck.qsui;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private Logger logger = Bukkit.getLogger();
    public static FileConfiguration config;
    public static File configFile;

    @Override
    public void onEnable() {
        super.onEnable();
        logger.log(Level.INFO, "QuickShop UI started!");
        this.initBDD();

        config = this.getConfig();
        config.options().copyDefaults(true);
        configFile = new File(this.getDataFolder(), "config.yml");
        this.saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(configFile);

        CommandExecutor quickshop = new PShop();
        this.getCommand("pshop").setExecutor(quickshop);
        this.getCommand("setpshop").setExecutor(quickshop);
        this.getCommand("delpshop").setExecutor(quickshop);
        Bukkit.getPluginManager().registerEvents(new InventoryListeners(), this);
    }

    private void initBDD() {
        String uri = this.getDataFolder().toString() + "\\database.db";
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + uri)) {
            if (connection != null) {
                DatabaseMetaData metaData = connection.getMetaData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
