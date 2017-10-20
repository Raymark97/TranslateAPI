package xyz.raymark.plugin.translateapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class TranslationProvider {
    Plugin plugin;
    TranslateAPI translateAPI;
    HashMap<Locale, HashMap<String, String>> translations = new HashMap<>();
    String prefix = "";


    public TranslationProvider(Plugin plugin) {
        this.plugin = plugin;
        this.translateAPI = (TranslateAPI) Bukkit.getPluginManager().getPlugin("TranslateAPI");
        translateAPI.getCommand("lang").setExecutor(new Commands(translateAPI, this));
        loadFiles();
    }

    public HashMap<Locale, HashMap<String, String>> getTranslations() {
        return translations;
    }

    LocalePlayer getLocalePlayer(Player player) {
        if(translateAPI.playerconf.containsKey(player.getUniqueId())) return translateAPI.playerconf.get(player.getUniqueId());
        return new LocalePlayer(player, Locale.forLanguageTag(player.getLocale()), this);
    }

    void loadFiles() {
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                TranslationFile file = new TranslationFile(plugin, locale);
                translations.put(locale, file.parseFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
