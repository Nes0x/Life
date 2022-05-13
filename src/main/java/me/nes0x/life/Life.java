package me.nes0x.life;


import me.nes0x.life.command.LifeCommand;
import me.nes0x.life.command.LifeCommandTabComplete;
import me.nes0x.life.listener.PlayerInteractListener;
import me.nes0x.life.listener.PlayerLoginListener;
import me.nes0x.life.listener.PlayerRespawnListener;
import me.nes0x.life.placeholderapi.Placeholder;
import me.nes0x.life.util.UpdateUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;

import static me.nes0x.life.util.DisplayUtil.fixColors;

public final class Life extends JavaPlugin {


    @Override
    public void onEnable() {
        new Metrics(this, 15178);
        new UpdateUtil(this, 0).getVersion(version -> {
            if (!getDescription().getVersion().equals(version)) {
                getLogger().info(ChatColor.RED + "New update available! https://www.spigotmc.org/resources/");
            }
        });

        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();
        saveResource("pl-config.yml", true);
        File usersFolder = new File(getDataFolder(), "./users");
        if (!usersFolder.exists()) {
            usersFolder.mkdir();
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholder(this).register();
        }

        getCommand("life").setExecutor(new LifeCommand(this));
        getCommand("life").setTabCompleter(new LifeCommandTabComplete());
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);


    }

}
