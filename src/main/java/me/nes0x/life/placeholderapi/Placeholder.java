package me.nes0x.life.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.nes0x.life.Life;
import me.nes0x.life.profile.PlayerProfile;
import me.nes0x.life.profile.PlayerProfileManager;
import org.bukkit.OfflinePlayer;

public class Placeholder extends PlaceholderExpansion {
    private final Life main;
    private final PlayerProfileManager playerProfileManager;

    public Placeholder(final Life main, final PlayerProfileManager playerProfileManager) {
        this.main = main;
        this.playerProfileManager = playerProfileManager;
    }

    @Override
    public String getIdentifier() {
        return "life";
    }

    @Override
    public String getAuthor() {
        return main.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return main.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }


    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (!player.hasPlayedBefore() && !player.isOnline()) {
            return null;
        }

        PlayerProfile profile = playerProfileManager.getByUUID(player.getUniqueId());

        if (profile == null) {
            return null;
        }

        int life = profile.getLife();
        if (params.equalsIgnoreCase("life-heart") || params.equalsIgnoreCase("life-number")) {
            if (life == 0) {
                return "Nie masz żyć";
            }
        }

        switch (params.toLowerCase()) {
            case "life-heart":
                StringBuilder hearts = new StringBuilder();
                for (int i = 0; i < life; i++) {
                    hearts.append("\u2665");
                }
                return hearts.toString();
            case "life-number":
                return String.valueOf(life);
        }
        return null;
    }
}
