package me.nes0x.life.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import me.nes0x.life.Main;

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
        return "Nes0x";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }


    @Override
    public String onRequest(OfflinePlayer player, String params) {
        LifeManager manager = new LifeManager(player.getUniqueId(), main);
        if (params.equalsIgnoreCase("life-hearts")){
            if (manager.getLife() == 0 || manager.getLife() == 1) {
                return DisplayUtils.fixColors(main.getConfig().getString("messages.no-life-placeholder"));
            }

            StringBuilder hearts = new StringBuilder();
            for (int i = 0; i < manager.getLife(); i++) {
                hearts.append(ChatColor.RED).append("\u2665");
            }
            return hearts.toString();
        }

        return null;
    }



}
