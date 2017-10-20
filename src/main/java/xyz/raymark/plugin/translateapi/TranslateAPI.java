package xyz.raymark.plugin.translateapi;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import static java.io.File.separator;
import static net.md_5.bungee.api.ChatColor.GREEN;
import static net.md_5.bungee.api.ChatColor.RED;

public class TranslateAPI extends JavaPlugin {

    private Server server = getServer();
    private FileConfiguration config = getConfig();
    private ConsoleCommandSender console = server.getConsoleSender();
    private Connection db = null;
    HashMap<UUID, LocalePlayer> playerconf = new HashMap<>();
    PreparedStatement setNewLocale;
    PreparedStatement getLocale;
    PreparedStatement updateLocale;

    public void onEnable() {
        //config
        config.addDefault("default", "EN_US");
        config.addDefault("MySQL.Enabled", false);
        config.addDefault("MySQL.IP", "localhost");
        config.addDefault("MySQL.Port", 3306);
        config.addDefault("MySQL.Database", "database");
        config.addDefault("MySQL.Username", "username");
        config.addDefault("MySQL.Password", "password");
        config.options().copyDefaults(true);
        saveConfig();

        //classes
        new Events(this);

        //message
        console.sendMessage("[TranslateAPI] Enabled!");

        //db
        try {
            // IF MySQL is enabled, load MySQL driver
            if(config.getBoolean("MySQL.Enabled")) {
                    Class.forName("com.mysql.jdbc.Driver");

                db = DriverManager.getConnection(
                        "jdbc:mysql://" + config.getString("MySQL.IP") +
                                ":" + config.getInt("MySQL.Port") +
                                "/" + config.getString("MySQL.Database"),
                        config.getString("MySQL.Username"),
                        config.getString("MySQL.Password")
                );
            } else {
                    // Load SQLite driver
                    db = DriverManager.getConnection("jdbc:sqlite:" + getDataFolder() + separator + "users.db");
            }
            if(db != null) {
                console.sendMessage(GREEN + "[TranslateAPI   ] Successfully connected to the database.");

                db.createStatement().execute("CREATE TABLE IF NOT EXISTS localePlayer (" +
                        "uuid VARCHAR(36)," +
                        "locale VARCHAR(8)," +
                        "PRIMARY KEY(uuid)" +
                        ");"
                );
                getLocale = db.prepareStatement("SELECT locale FROM localePlayer WHERE uuid= ? ");
                setNewLocale = db.prepareStatement("INSERT INTO localePlayer VALUES (? , ?)");
                updateLocale = db.prepareStatement("UPDATE localePlayer SET locale=? WHERE player=?");
            } else {
                console.sendMessage(RED + "[TranslateAPI] Could not connect to the database!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public ConsoleCommandSender getConsole() {
        return console;
    }

}
