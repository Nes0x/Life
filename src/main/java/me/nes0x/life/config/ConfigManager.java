package me.nes0x.life.config;

import me.nes0x.life.Life;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

public class ConfigManager {
    private final Life main;
    private FileConfiguration config;
    private YamlConfiguration messagesLanguage;

    public ConfigManager(final Life main) {
        this.main = main;
        this.config = main.getConfig();
        checkVersion();
        File messagesLanguageFile = new File(main.getDataFolder(), "messages-" + config.getString("language") + ".yml");
        if (!messagesLanguageFile.exists()) {
            Bukkit.getLogger().log(Level.SEVERE, "[" + main.getName() + "] Language which you are choose is not exists!");
            Bukkit.getPluginManager().disablePlugin(main);
            return;
        }
        this.messagesLanguage = YamlConfiguration.loadConfiguration(messagesLanguageFile);
    }

    private void checkVersion() {
        if (!config.getString("version").equalsIgnoreCase(main.getDescription().getVersion())) {
            long timestamp = System.currentTimeMillis();
            try {
                Files.walk(Paths.get(main.getDataFolder().getPath())).filter(Files::isRegularFile)
                        .forEach(file -> {
                            if (file.toFile().getName().contains("messages")) {
                                file.toFile().renameTo(new File(main.getDataFolder(), timestamp + "-old-" + file.toFile().getName()));
                            }
                        });
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            config.set("version", main.getDescription().getVersion());
            main.saveConfig();
            main.reloadConfig();
            main.saveResource("messages-pl.yml", true);
            main.saveResource("messages-en.yml", true);
        }
    }

    public YamlConfiguration getMessagesLanguage() {
        return messagesLanguage;
    }

    public void reload() {
        main.reloadConfig();
        main.saveConfig();
        config = main.getConfig();
        File languageFile = new File(main.getDataFolder(), "messages-" + config.getString("language") + ".yml");
        messagesLanguage = YamlConfiguration.loadConfiguration(languageFile);
    }

    public String getMessage(ConfigMessage message) {
        return ChatColor.translateAlternateColorCodes('&', messagesLanguage.getString(message.getPath()));
    }

    public Object getOption(ConfigOption option) {
        return config.get(option.getPath());
    }
}
