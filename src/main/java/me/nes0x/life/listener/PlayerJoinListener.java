package me.nes0x.life.listener;

import me.nes0x.life.Life;
import me.nes0x.life.profile.PlayerProfileManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlayerJoinListener implements Listener {
    private final Life main;
    private final PlayerProfileManager playerProfileManager;

    public PlayerJoinListener(final Life main, final PlayerProfileManager playerProfileManager) {
        this.main = main;
        this.playerProfileManager = playerProfileManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!playerProfileManager.exists(player.getUniqueId())) {
            playerProfileManager.add(player.getUniqueId());
            File playersFile = new File(main.getDataFolder(), "players.yml");
            YamlConfiguration players = YamlConfiguration.loadConfiguration(playersFile);
            List<String> uuids = players.getStringList("uuids");
            if (!uuids.contains(player.getUniqueId().toString())) {
                uuids.add(player.getUniqueId().toString());
                players.set("uuids", uuids);
                try {
                    players.save(playersFile);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}
