package me.nes0x.life.listeners;


import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
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
    public void onPlayerDeath(PlayerDeathEvent event) {
        FileConfiguration config = main.getConfig();
        Player player = event.getEntity();
        if (config.getBoolean("world-guard.enabled")) {
            if (isOnRegion(player)) {
                return;
            }
        }
        LifeManager manager = new LifeManager(player.getUniqueId(), main);
        int number = config.getInt("settings.remove-life-on-death-number");
        manager.removeLife(number);
        if (config.getBoolean("settings.enable-message-on-death")) {
            player.sendMessage(fixColors(config.getString("messages.message-on-death").replace("%number%", String.valueOf(number))));
        }
    }

}
