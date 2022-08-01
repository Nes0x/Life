package me.nes0x.life.util;

import me.nes0x.life.config.ConfigManager;
import me.nes0x.life.config.ConfigMessage;

public class DisplayUtil {

    public static String minutesToTime(int time, ConfigManager config) {
        StringBuilder timeToReturn = new StringBuilder();

        if (time / 1440 > 0) {
            timeToReturn.append(time / 1440).append(" ").append(config.getMessage(ConfigMessage.DAYS)).append(" ");
            time -= (time / 1440) * 1440;
        }

        if (time / 60 > 0 && !(time / 1440 > 0)) {
            timeToReturn.append(time / 60).append(" ").append(config.getMessage(ConfigMessage.HOURS)).append(" ");
            time -= (time / 60) * 60;
        }

        if (time == 0) {
            timeToReturn.append("1 ").append(config.getMessage(ConfigMessage.MINUTES)).append(" ");
        } else {
            timeToReturn.append(time).append(" ").append(config.getMessage(ConfigMessage.MINUTES)).append(" ");
        }

        return timeToReturn.toString();
    }
}
