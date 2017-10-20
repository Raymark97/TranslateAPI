package xyz.raymark.plugin.translateapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Locale;

public class LocalePlayer {
    private final TranslateAPI translateAPI;
    private TranslationProvider classe;
    private Player player;
    private Locale locale;

    public LocalePlayer(Player player, Locale locale, TranslationProvider classe) {
        this.player = player;
        this.locale = locale;
        this.classe = classe;
        this.translateAPI = ((TranslateAPI) Bukkit.getPluginManager().getPlugin("TranslateAPI"));
    }


    public void setLocale(Locale locale){
        this.locale = locale;
        try {
            translateAPI.updateLocale.setString(1, player.getUniqueId().toString());
            translateAPI.updateLocale.setString(2, locale.toLanguageTag());
            translateAPI.updateLocale.executeQuery();
            translateAPI.playerconf.put(player.getUniqueId(), this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Locale getLocale() {
        return locale;
    }

    public void sendMessage(String string) {
        if(!classe.translations.containsKey(locale)) locale = Locale.forLanguageTag(classe.plugin.getConfig().getString("default"));
        player.sendMessage(classe.prefix + classe.translations.get(locale).get(string));
    }
}
