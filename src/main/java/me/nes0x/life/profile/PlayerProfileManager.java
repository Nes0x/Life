package me.nes0x.life.profile;

import me.nes0x.life.config.ConfigManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerProfileManager {
    private final ConfigManager config;
    private final static Map<UUID, PlayerProfile> playerProfiles = new HashMap<>();

    public PlayerProfileManager(final ConfigManager config) {
        this.config = config;
    }

    public void add(UUID uuid) {
        playerProfiles.put(uuid, new PlayerProfile(uuid, config));
    }

    public boolean exists(UUID uuid) {
        return playerProfiles.containsKey(uuid);
    }

    public PlayerProfile getByUUID(UUID uuid) {
        if (exists(uuid)) {
            return playerProfiles.get(uuid);
        }
        return null;
    }
}
