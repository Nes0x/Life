package me.nes0x.life.listeners;


import me.nes0x.life.utils.LifeManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import me.nes0x.life.Main;

import static me.nes0x.life.utils.DisplayUtils.fixColors;


public class PlayerDeath implements Listener {
    private final Main main;

    public PlayerDeath(final Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        FileConfiguration config = main.getConfig();
        Player player = event.getEntity();
        LifeManager manager = new LifeManager(player.getUniqueId(), main);
        int number = config.getInt("settings.remove-life-on-death-number");
        manager.removeLife(number);
        if (config.getBoolean("settings.enable-message-on-death")) {
            player.sendMessage(fixColors(config.getString("messages.message-on-death").replace("%number%", String.valueOf(number))));
        }
    }

}
