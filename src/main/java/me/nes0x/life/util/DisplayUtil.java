package me.nes0x.life.util;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class DisplayUtil {

    public static String fixColors(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String minutesToTime(int time, FileConfiguration config) {
        StringBuilder timeToReturn = new StringBuilder();

        if (time / 1440 > 0) {
            timeToReturn.append(time / 1440).append(" ").append(fixColors(config.getString("messages.days"))).append(" ");
            time -= (time / 1440) * 1440;
        }

        if (time / 60 > 0 && !(time / 1440 > 0)) {
            timeToReturn.append(time / 60).append(" ").append(fixColors(config.getString("messages.hours"))).append(" ");
            time -= (time / 60) * 60;
        }

        if (time == 0) {
            timeToReturn.append("1 ").append(fixColors(config.getString("messages.minutes"))).append(" ");
        } else {
            timeToReturn.append(time).append(" ").append(fixColors(config.getString("messages.minutes"))).append(" ");
        }
        return timeToReturn.toString();
    }
}
