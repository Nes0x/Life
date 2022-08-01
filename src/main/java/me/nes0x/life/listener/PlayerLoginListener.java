package me.nes0x.life.listener;

import me.nes0x.life.config.ConfigManager;
import me.nes0x.life.config.ConfigMessage;
import me.nes0x.life.profile.PlayerProfile;
import me.nes0x.life.profile.PlayerProfileManager;
import me.nes0x.life.util.DisplayUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {
    private final PlayerProfileManager playerProfileManager;
    private final ConfigManager config;

    public PlayerLoginListener(final PlayerProfileManager playerProfileManager, final ConfigManager config) {
        this.playerProfileManager = playerProfileManager;
        this.config = config;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = playerProfileManager.getByUUID(player.getUniqueId());

        if (profile == null) {
            return;
        }

        if (profile.getLife() > 0) {
            event.allow();
            profile.setPerm(false);
            profile.setBanExpiration(0);
        } else {
            if (profile.isPerm()) {
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, config.getMessage(ConfigMessage.PERM_BAN_REASON));
            } else {
                int minutes = profile.getBanTime();
                if (minutes < 0) {
                    event.allow();
                    profile.setBanExpiration(0);
                } else {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, config.getMessage(ConfigMessage.TEMP_BAN_REASON)
                            .replace("%time%", DisplayUtil.minutesToTime(minutes, config)));
                }
            }
        }
    }
}
