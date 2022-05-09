package me.nes0x.life.listeners;

import me.nes0x.life.utils.DisplayUtils;
import me.nes0x.life.utils.LifeManager;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import me.nes0x.life.Main;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PlayerLogin implements Listener {
    private final Main main;

    public PlayerLogin(final Main main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent event) {
        FileConfiguration config = main.getConfig();
        Player player = event.getPlayer();
        LifeManager manager = new LifeManager(player.getUniqueId(), main);


        if (manager.getLife() > 0) {
            event.allow();
            manager.setPerm(false);
            manager.setBanExpiration(0);
        } else {
            if (manager.isPerm()) {
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, DisplayUtils.fixColors(config.getString("messages.perm-ban-reason")));
            } else {
                int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(manager.getBanExpiration() - System.currentTimeMillis());
                if (minutes < 0) {
                    event.allow();
                    manager.setBanExpiration(0);
                } else {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, DisplayUtils.fixColors(config.getString("messages.temp-ban-reason").replace("%time%", DisplayUtils.minutesToTime(minutes, config))));
                }
            }




        }





    }

}
