package me.nes0x.life.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.nes0x.life.Main;
import org.bukkit.OfflinePlayer;

public class Placeholders extends PlaceholderExpansion {
    private final Main main;

    public Placeholders(final Main main) {
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
        if (!player.hasPlayedBefore()) {
            return  null;
        }
        LifeManager manager = new LifeManager(player.getUniqueId(), main);

        if (params.equalsIgnoreCase("life-heart") || params.equalsIgnoreCase("life-number")) {
            if (manager.getLife() == 0 || manager.getLife() == 1) {
                return DisplayUtils.fixColors(main.getConfig().getString("messages.no-life-placeholder"));
            }
        }

        switch (params.toLowerCase()) {
            case "life-heart":
                return "\u2665".repeat(Math.max(0, manager.getLife()));
            case "life-number":
                return String.valueOf(manager.getLife());
        }
        return null;
    }



}
