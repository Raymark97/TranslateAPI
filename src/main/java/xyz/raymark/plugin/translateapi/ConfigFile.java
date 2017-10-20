package xyz.raymark.plugin.translateapi;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ConfigFile extends File {

    private List<String> strings = new ArrayList<>();
    private HashMap<Locale, Boolean> langs = new HashMap<>();

    private Yaml yaml = new Yaml();

    @SuppressWarnings("unchecked")
    public ConfigFile(String pathname) throws IOException {
        super(pathname);
        if(this.exists()) {
            HashMap<String, Object> map = (HashMap<String, Object>) yaml.load(new FileReader(this));
            strings = (List<String>) map.get("defaults");
            langs = (HashMap<Locale, Boolean>) map.get("languages");
        } else {
            this.createNewFile();
        }
        for(Locale locale : Locale.getAvailableLocales()) {
            if(!langs.containsKey(locale)) langs.put(locale, false);
        }
    }

    public void addDefault(String string) {
        strings.add(string);
    }

    public void enableLang(Locale locale, Boolean bool) {
        langs.put(locale, bool);
    }

    public void saveFile() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("languages", langs);
        map.put("defaults", strings);
        yaml.dump(map);
    }
}
