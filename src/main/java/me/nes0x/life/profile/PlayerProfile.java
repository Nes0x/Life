package me.nes0x.life.profile;

import me.nes0x.life.config.ConfigManager;
import me.nes0x.life.config.ConfigOption;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerProfile {
    private final OfflinePlayer offlinePlayer;
    private YamlConfiguration playerData;
    private final File playerFile;
    private int life;
    private boolean perm;
    private long banExpiration;

    protected PlayerProfile(UUID playerUUID, ConfigManager config) {
        offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
        playerFile = new File("./plugins/Life/players/" + playerUUID + ".yml");
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
                playerData = YamlConfiguration.loadConfiguration(playerFile);
                playerData.set("life", config.getOption(ConfigOption.SETTINGS_STARTING_LIFE_NUMBER));
                playerData.set("perm", false);
                playerData.set("ban-expiration", 0);
                playerData.save(playerFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            playerData = YamlConfiguration.loadConfiguration(playerFile);
        }

        life = playerData.getInt("life");
        if (life < 0) life = 0;
        perm = playerData.getBoolean("perm");
        banExpiration = playerData.getLong("ban-expiration");
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }

    public int getLife() {
        return life;
    }

    public boolean isPerm() {
        return perm;
    }

    public long getBanExpiration() {
        return banExpiration;
    }

    public int getBanTime() {
        return (int) TimeUnit.MILLISECONDS.toMinutes(banExpiration - System.currentTimeMillis());
    }

    public void setPerm(boolean perm) {
        playerData.set("perm", perm);
        this.perm = perm;
        try {
            playerData.save(playerFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setBanExpiration(long banExpiration) {
        playerData.set("ban-expiration", banExpiration);
        this.banExpiration = banExpiration;
        try {
            playerData.save(playerFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setLife(int life) {
        if (life < 0) {
            throw new IllegalStateException();
        }

        playerData.set("life", life);
        this.life = life;
        try {
            playerData.save(playerFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void removeLife(int number) {
        if ((life - number) < 0) {
            setLife(0);
            return;
        }
        playerData.set("life", life - number);
        life -= number;
        try {
            playerData.save(playerFile);
        } catch (IOException exception) {
           exception.printStackTrace();
        }
    }

    public void addLife(int number) {
        playerData.set("life", life + number);
        life += number;
        try {
            playerData.save(playerFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
