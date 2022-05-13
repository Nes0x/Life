package me.nes0x.life.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.nes0x.life.Life;
import me.nes0x.life.manager.LifeManager;
import me.nes0x.life.util.DisplayUtil;
import org.bukkit.OfflinePlayer;

public class Placeholder extends PlaceholderExpansion {
    private final Life main;

    public Placeholder(final Life main) {
        this.main = main;
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
            return  null;
        }
        LifeManager manager = new LifeManager(player.getUniqueId(), main);

        if (params.equalsIgnoreCase("life-heart") || params.equalsIgnoreCase("life-number")) {
            if (manager.getLife() == 0) {
                return DisplayUtil.fixColors(main.getConfig().getString("messages.no-life-placeholder"));
            }
        }

        switch (params.toLowerCase()) {
            case "life-heart":
                StringBuilder hearts = new StringBuilder();
                for (int i = 0; i < manager.getLife(); i++) {
                    hearts.append("\u2665");
                }
                return hearts.toString();
            case "life-number":
                return String.valueOf(manager.getLife());
        }
        return null;
    }



}
