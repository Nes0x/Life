package me.nes0x.life.listeners;

import me.nes0x.life.utils.DisplayUtils;
import me.nes0x.life.utils.LifeManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import me.nes0x.life.Main;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static me.nes0x.life.utils.DisplayUtils.fixColors;

public class PlayerRespawn implements Listener {
    private final Main main;

    public PlayerRespawn(final Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();
        LifeManager manager = new LifeManager(player.getUniqueId(), main);


        if (manager.getLife() == 0) {
            FileConfiguration config = main.getConfig();
            String kickMessage;
            if (config.getBoolean("ban.perm")) {
                manager.setPerm(true);
                kickMessage = config.getString("messages.perm-ban-reason");
            } else {
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, config.getInt("ban.day"));
                calendar.add(Calendar.HOUR_OF_DAY, config.getInt("ban.hours"));
                calendar.add(Calendar.MINUTE, config.getInt("ban.minutes"));
                long banDate = calendar.getTimeInMillis();
                manager.setBanExpiration(banDate);
                kickMessage = config.getString("messages.temp-ban-reason").replace("%time%",
                        DisplayUtils.minutesToTime(
                        (int)TimeUnit.MILLISECONDS.toMinutes(
                        banDate - System.currentTimeMillis()
                ), config));
            }

            Bukkit.getScheduler().runTaskLater(main, () -> {
                player.kickPlayer(fixColors(kickMessage));
            }, 20);
        }
    }

}
