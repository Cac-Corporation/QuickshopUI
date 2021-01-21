package com.cactt4ck.qsui;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private Logger logger = Bukkit.getLogger();
    public static FileConfiguration config;
    public static File configFile;
    private Connection connection;

    @Override
    public void onEnable() {
        super.onEnable();
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "[QuickshopUI] QuickShop UI started!");
        this.initBDD();

        config = this.getConfig();
        config.options().copyDefaults(true);
        configFile = new File(this.getDataFolder(), "config.yml");
        this.saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(configFile);

        CommandExecutor quickshop = new PShop(this);
        TabCompleter tabCompleter = new QsUITabCompleter();
        this.getCommand("pshop").setExecutor(quickshop);
        this.getCommand("pshop").setTabCompleter(tabCompleter);
        Bukkit.getPluginManager().registerEvents(new InventoryListeners(this), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        try {
            this.connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void initBDD() {
        String uri = this.getDataFolder() + "/database.db";
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + uri);
            if (connection != null) {
                this.connection = connection;
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet resultSet = metaData.getTables(null, null, "shop_location", null);
                if (!resultSet.next()){
                    String createTableQuery = "create table shop_location (id INTEGER constraint shop_location_pk primary key autoincrement, playername TEXT, world TEXT, x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT)";
                    Statement statement = connection.createStatement();
                    statement.execute(createTableQuery);
                    logger.log(Level.INFO, "[QuickshopUI] §cDatabase not found! Creating one.");
                } else
                    logger.log(Level.INFO, "[QuickshopUI] §aDatabase found! Loading it.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
