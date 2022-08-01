package me.nes0x.life.listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.nes0x.life.Life;
import me.nes0x.life.config.ConfigManager;
import me.nes0x.life.config.ConfigMessage;
import me.nes0x.life.config.ConfigOption;
import me.nes0x.life.profile.PlayerProfile;
import me.nes0x.life.profile.PlayerProfileManager;
import me.nes0x.life.util.DisplayUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PlayerRespawnListener implements Listener {
    private final Life main;
    private final PlayerProfileManager playerProfileManager;
    private final ConfigManager config;

    public PlayerRespawnListener(final Life main, final PlayerProfileManager playerProfileManager, final ConfigManager config) {
        this.main = main;
        this.playerProfileManager = playerProfileManager;
        this.config = config;
    }

    private boolean isOnRegion(Player player) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(player.getWorld()));
        for (ProtectedRegion region : regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()))) {
            List<String> regions = (List<String>) config.getOption(ConfigOption.WORLD_GUARD_REGIONS);
            if (regions.contains(region.getId().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = playerProfileManager.getByUUID(player.getUniqueId());

        if (profile == null) {
            return;
        }

        if (profile.getLife() == 0) {
            String kickMessage;
            if ((boolean) config.getOption(ConfigOption.BAN_PERM)) {
                profile.setPerm(true);
                kickMessage = config.getMessage(ConfigMessage.PERM_BAN_REASON);
            } else {
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, (int) config.getOption(ConfigOption.BAN_DAYS));
                calendar.add(Calendar.HOUR_OF_DAY, (int) config.getOption(ConfigOption.BAN_HOURS));
                calendar.add(Calendar.MINUTE, (int) config.getOption(ConfigOption.BAN_MINUTES));
                long banDate = calendar.getTimeInMillis();
                profile.setBanExpiration(banDate);
                kickMessage = config.getMessage(ConfigMessage.TEMP_BAN_REASON)
                        .replace("%time%", DisplayUtil.minutesToTime(profile.getBanTime(), config));
            }

            Bukkit.getScheduler().runTaskLater(main, () -> {
                player.kickPlayer(kickMessage);
            }, 20);
        }

        if ((boolean) config.getOption(ConfigOption.WORLD_GUARD_ENABLED)) {
            if (isOnRegion(player)) {
                return;
            }
        }
        int number = (int) config.getOption(ConfigOption.SETTINGS_REMOVE_LIFE_ON_DEATH_NUMBER);
        profile.removeLife(number);
        if ((boolean) config.getOption(ConfigOption.SETTINGS_ENABLE_MESSAGE_ON_DEATH)) {
            player.sendMessage(config.getMessage(ConfigMessage.MESSAGE_ON_DEATH)
                    .replace("%number%", String.valueOf(number)));
        }
    }
}
