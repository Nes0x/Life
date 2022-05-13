package me.nes0x.life.manager;

import me.nes0x.life.Life;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LifeManager {
    private YamlConfiguration userData;
    private final File user;
    private int life;
    private boolean perm;
    private long banExpiration;


    public LifeManager(UUID playerUUID, Life main) {
        user = new File(main.getDataFolder(), "./users/" + playerUUID + ".yml");
        if (!user.exists()) {
            try {
                user.createNewFile();
                userData = YamlConfiguration.loadConfiguration(user);
                userData.set("life", main.getConfig().getInt("settings.starting-life-number"));
                userData.set("perm", false);
                userData.set("ban-expiration", 0);
                userData.save(user);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            userData = YamlConfiguration.loadConfiguration(user);
        }

        life = userData.getInt("life");
        if (life < 0) life = 0;
        perm = userData.getBoolean("perm");
        banExpiration = userData.getLong("ban-expiration");
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
        userData.set("perm", perm);
        this.perm = perm;
        try {
            userData.save(user);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setBanExpiration(long banExpiration) {
        userData.set("ban-expiration", banExpiration);
        this.banExpiration = banExpiration;
        try {
            userData.save(user);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setLife(int life) {
        if (life < 0) {
            throw new IllegalStateException();
        }

        userData.set("life", life);
        this.life = life;
        try {
            userData.save(user);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void removeLife(int number) {
        if ((life - number) < 0) {
            setLife(0);
            return;
        }

        userData.set("life", life - number);
        life -= number;
        try {
            userData.save(user);
        } catch (IOException exception) {
           exception.printStackTrace();
        }
    }

    public void addLife(int number) {
        if (number <= 0) {
            throw new IllegalStateException();
        }

        userData.set("life", life + number);
        life += number;
        try {
            userData.save(user);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
