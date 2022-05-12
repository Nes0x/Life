package me.nes0x.life;


import me.nes0x.life.command.LifeCommand;
import me.nes0x.life.command.LifeTabComplete;
import me.nes0x.life.listener.PlayerInteractListener;
import me.nes0x.life.listener.PlayerLoginListener;
import me.nes0x.life.listener.PlayerRespawnListener;
import me.nes0x.life.util.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

import static me.nes0x.life.util.DisplayUtil.fixColors;

public final class Life extends JavaPlugin {


    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();
        File usersFolder = new File(getDataFolder(), "./users");
        if (!usersFolder.exists()) {
            usersFolder.mkdir();
        }

        OutputStream outStream = null;
        try {
            byte[] buffer = getResource("pl-config.yml").readAllBytes();
            File targetFile = new File(getDataFolder(), "./pl-config.yml");
            outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (outStream != null) outStream.close();
            } catch (IOException exception) {
                Bukkit.getLogger().info(fixColors("&cFatal error occurred!"));
                getPluginLoader().disablePlugin(this);
            }
        }


        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholder(this).register();
        }

        getCommand("life").setExecutor(new LifeCommand(this));
        getCommand("life").setTabCompleter(new LifeTabComplete());
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);


    }

}
