package xyz.raymark.plugin.translateapi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class Commands implements CommandExecutor {
    private final TranslateAPI plugin;
    private TranslationProvider tp;

    public Commands(TranslateAPI plugin, TranslationProvider translationProvider) {
        this.plugin = plugin;
        this.tp = translationProvider;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("translateapi")) return false;
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "reload":
                    tp.loadFiles();
                    break;
                case "set":
                    if(sender instanceof Player) {
                        if (args.length == 2) {
                            TranslationProvider tp = new TranslationProvider(plugin);
                            LocalePlayer localePlayer = tp.getLocalePlayer((Player) sender);
                            Locale locale = Locale.forLanguageTag(args[1]);
                            if (locale != null) {
                                localePlayer.setLocale(locale);
                            } else {
                                sender.sendMessage("invalid locale");
                            }
                        } else {
                            sender.sendMessage("usage /translateapi set locale");
                        }
                    } else {
                        sender.sendMessage("Il comando pul essere eseguito solo da un player.");
                    }
                    break;
                default:
                    break;
            }
        }
        return true;
    }
}