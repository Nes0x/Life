package me.nes0x.life.listener;

import me.nes0x.life.util.DisplayUtil;
import me.nes0x.life.manager.LifeManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import me.nes0x.life.Life;

public class PlayerLoginListener implements Listener {
    private final Life main;

    public PlayerLoginListener(final Life main) {
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
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, DisplayUtil.fixColors(config.getString("messages.perm-ban-reason")));
            } else {
                int minutes = manager.getBanTime();
                if (minutes < 0) {
                    event.allow();
                    manager.setBanExpiration(0);
                } else {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, DisplayUtil.fixColors(config.getString("messages.temp-ban-reason").replace("%time%", DisplayUtil.minutesToTime(minutes, config))));
                }
            }




        }





    }

}
