package xyz.raymark.plugin.translateapi;

import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;

public class TranslationFile extends File {

    private Yaml yaml = new Yaml();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public TranslationFile(Plugin plugin, Locale locale) throws IOException {
        super(plugin.getDataFolder() + File.separator + "translations" + File.separator + locale.toLanguageTag() + ".yml");
        if(!getAbsoluteFile().exists()) createNewFile();
    }

    @SuppressWarnings("unchecked")
    HashMap<String, String> parseFile() throws FileNotFoundException {
            return (HashMap<String, String>) yaml.load(new FileReader(this));
    }
}
