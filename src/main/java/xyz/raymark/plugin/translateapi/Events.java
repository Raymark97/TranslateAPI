package xyz.raymark.plugin.translateapi;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.Locale;

public class Events implements Listener {

    private final TranslateAPI plugin;

    public Events(TranslateAPI plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {
        Player p = e.getPlayer();
        LocalePlayer localePlayer;
        plugin.getLocale.setString(1, p.getUniqueId().toString());
        String langTag = plugin.getLocale.executeQuery().getString(0);
        if(langTag == null) {
            langTag = p.getLocale();
            plugin.setNewLocale.setString(1, p.getUniqueId().toString());
            plugin.setNewLocale.setString(2, p.getLocale());
            plugin.setNewLocale.execute();
        }
        localePlayer = new LocalePlayer(p, Locale.forLanguageTag(langTag), new TranslationProvider(plugin));
        plugin.playerconf.put(p.getUniqueId(), localePlayer);
    }
}
