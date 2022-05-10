package me.nes0x.life;


import me.nes0x.life.commands.Life;
import me.nes0x.life.commands.LifeTabComplete;
import me.nes0x.life.listeners.PlayerDeath;
import me.nes0x.life.listeners.PlayerInteract;
import me.nes0x.life.listeners.PlayerLogin;
import me.nes0x.life.listeners.PlayerRespawn;
import me.nes0x.life.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        File dataFolder = getDataFolder();
        File usersFolder = new File(getDataFolder(), "./users");
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        if (!usersFolder.exists()) {
            usersFolder.mkdir();
        }
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders(this).register();
        }
        getCommand("life").setExecutor(new Life(this));
        getCommand("life").setTabCompleter(new LifeTabComplete());
        getServer().getPluginManager().registerEvents(new PlayerLogin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawn(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);


    }

}
