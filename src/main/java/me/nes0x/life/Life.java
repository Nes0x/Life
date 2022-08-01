package me.nes0x.life;

import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.components.TypeResult;

import me.nes0x.life.command.LifeCommand;
import me.nes0x.life.config.*;
import me.nes0x.life.listener.*;
import me.nes0x.life.placeholderapi.Placeholder;
import me.nes0x.life.profile.*;
import me.nes0x.life.util.UpdateUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

public final class Life extends JavaPlugin {
    private PlayerProfileManager playerProfileManager;
    private ConfigManager config;

    @Override
    public void onEnable() {
        new Metrics(this, 15178);
        new UpdateUtil(this, 101976).getVersion(version -> {
            if (!getDescription().getVersion().equals(version)) {
                getLogger().info(ChatColor.RED + "New update available! https://www.spigotmc.org/resources/life.101976/");
            }
        });

        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();

        saveResource("players.yml", false);
        saveResource("messages-pl.yml", false);
        saveResource("messages-en.yml", false);

        config = new ConfigManager(this);
        if (config.getMessagesLanguage() == null) {
            return;
        }

        playerProfileManager = new PlayerProfileManager(config);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholder(this, playerProfileManager).register();
        }

        initFiles();
        registerEvents();
        registerCommands();
    }

    private void initFiles() {
        File playersDir = new File(getDataFolder(), "./players");
        if (!playersDir.exists()) {
            playersDir.mkdir();
        }

        File playersFile = new File(getDataFolder(), "players.yml");
        YamlConfiguration players = YamlConfiguration.loadConfiguration(playersFile);

        for (String uuid : players.getStringList("uuids")) {
            playerProfileManager.add(UUID.fromString(uuid));
        }
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(playerProfileManager, config), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, playerProfileManager), this);
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(playerProfileManager, config), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this, playerProfileManager, config), this);
    }

    private void registerCommands() {
        CommandManager commandManager = new CommandManager(this);
        commandManager.hideTabComplete(true);

        commandManager.getParameterHandler().register(PlayerProfile.class, argument -> {
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(String.valueOf(argument));
            if (offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline()) {
                if (playerProfileManager.exists(offlinePlayer.getUniqueId()))
                    return new TypeResult(playerProfileManager.getByUUID(offlinePlayer.getUniqueId()), argument);
            }
            return new TypeResult(argument);
        });

        commandManager.getMessageHandler().register("cmd.no.console", sender -> {
            sender.sendMessage(config.getMessage(ConfigMessage.COMMAND_IN_CONSOLE));
        });

        commandManager.getMessageHandler().register("cmd.no.permission", sender -> {
            sender.sendMessage(config.getMessage(ConfigMessage.NO_PERMISSION));
        });

        commandManager.getMessageHandler().register("cmd.no.exists", sender -> {
            sender.sendMessage(config.getMessage(ConfigMessage.COMMAND_NOT_EXISTS));
        });

        commandManager.getMessageHandler().register("cmd.wrong.usage", sender -> {
            sender.sendMessage(config.getMessage(ConfigMessage.INVALID_USAGE));
        });

        commandManager.register(new LifeCommand(playerProfileManager, config));
    }
}
