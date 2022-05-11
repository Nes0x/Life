package me.nes0x.life.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
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

    private boolean isOnRegion(Player player) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(player.getWorld()));
        for (ProtectedRegion region : regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()))) {
            if (main.getConfig().getStringList("world-guard.regions").contains(region.getId().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        FileConfiguration config = main.getConfig();
        Player player = event.getPlayer();
        LifeManager manager = new LifeManager(player.getUniqueId(), main);


        if (manager.getLife() == 0) {
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
                        manager.getBanTime(), config));
            }

            Bukkit.getScheduler().runTaskLater(main, () -> {
                player.kickPlayer(fixColors(kickMessage));
            }, 20);
        }

        if (config.getBoolean("world-guard.enabled")) {
            if (isOnRegion(player)) {
                return;
            }
        }
        int number = config.getInt("settings.remove-life-on-death-number");
        manager.removeLife(number);
        if (config.getBoolean("settings.enable-message-on-death")) {
            player.sendMessage(fixColors(config.getString("messages.message-on-death").replace("%number%", String.valueOf(number))));
        }
    }

}
