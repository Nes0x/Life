package me.nes0x.life.command;

import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import me.nes0x.life.config.ConfigManager;
import me.nes0x.life.config.ConfigMessage;
import me.nes0x.life.config.ConfigOption;
import me.nes0x.life.profile.PlayerProfile;
import me.nes0x.life.profile.PlayerProfileManager;
import me.nes0x.life.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


@Command("life")
public class LifeCommand extends CommandBase {
    private final PlayerProfileManager playerProfileManager;
    private final ConfigManager config;

    public LifeCommand(final PlayerProfileManager playerProfileManager, final ConfigManager config) {
        this.playerProfileManager = playerProfileManager;
        this.config = config;
    }

    @Default
    @Completion("#players")
    public void life(final Player player, @Optional final String targetNick) {
        PlayerProfile playerProfile = null;
        String nick = "";
        if (targetNick == null) {
            playerProfile = playerProfileManager.getByUUID(player.getUniqueId());
            nick = player.getName();
        } else {
            OfflinePlayer targetProfile = Bukkit.getOfflinePlayer(targetNick);
            if (targetProfile.hasPlayedBefore() || targetProfile.isOnline()) {
                playerProfile = playerProfileManager.getByUUID(targetProfile.getUniqueId());
                nick = targetNick;
            }
        }

        if (playerProfile == null) {
            player.sendMessage(config.getMessage(ConfigMessage.UNKNOWN_PLAYER));
            return;
        }

        player.sendMessage(config.getMessage(ConfigMessage.NUMBER_OF_LIFE).replace("%player%", nick).replace("%life%", String.valueOf(playerProfile.getLife())));
    }

    @SubCommand("add")
    @Permission("life.command.add")
    @Completion({"#players", "#range:1-100"})
    public void addLife(final Player player, final PlayerProfile targetProfile, final Integer amount) {
        if (targetProfile == null) {
            player.sendMessage(config.getMessage(ConfigMessage.UNKNOWN_PLAYER));
            return;
        }
        try {
            if (amount <= 0) {
                player.sendMessage(config.getMessage(ConfigMessage.LIFE_NUMBER_EXCEPTION));
                return;
            }
        } catch (Exception exception) {
            player.sendMessage(config.getMessage(ConfigMessage.NUMBER_EXCEPTION));
            return;
        }
        targetProfile.addLife(amount);
        player.sendMessage(config.getMessage(ConfigMessage.ADD_LIFE_SUCCESS).replace("%player%", targetProfile.getOfflinePlayer().getName()).replace("%number%", String.valueOf(amount)));
    }

    @SubCommand("remove")
    @Permission("life.command.remove")
    @Completion({"#players", "#range:1-100"})
    public void removeLife(final Player player, final PlayerProfile targetProfile, final Integer amount) {
        if (targetProfile == null) {
            player.sendMessage(config.getMessage(ConfigMessage.UNKNOWN_PLAYER));
            return;
        }
        try {
            if (amount <= 0 || (targetProfile.getLife() - amount) < 0) {
                player.sendMessage(config.getMessage(ConfigMessage.LIFE_NUMBER_EXCEPTION));
                return;
            }
        } catch (Exception exception) {
            player.sendMessage(config.getMessage(ConfigMessage.NUMBER_EXCEPTION));
            return;
        }
        targetProfile.removeLife(amount);
        player.sendMessage(config.getMessage(ConfigMessage.REMOVE_LIFE_SUCCESS).replace("%player%", targetProfile.getOfflinePlayer().getName()).replace("%number%", String.valueOf(amount)));
    }

    @SubCommand("set")
    @Permission("life.command.set")
    @Completion({"#players", "#range:1-100"})
    public void setLife(final Player player, final PlayerProfile targetProfile, final Integer amount) {
        if (targetProfile == null) {
            player.sendMessage(config.getMessage(ConfigMessage.UNKNOWN_PLAYER));
            return;
        }
        try {
            if (amount < 0) {
                player.sendMessage(config.getMessage(ConfigMessage.LIFE_NUMBER_EXCEPTION));
                return;
            }
        } catch (Exception exception) {
            player.sendMessage(config.getMessage(ConfigMessage.NUMBER_EXCEPTION));
            return;
        }
        targetProfile.setLife(amount);
        player.sendMessage(config.getMessage(ConfigMessage.SET_LIFE_SUCCESS).replace("%player%", targetProfile.getOfflinePlayer().getName()).replace("%number%", String.valueOf(amount)));
    }

    @SubCommand("give")
    @Permission("life.command.give")
    @Completion({"#players", "#range:1-100"})
    public void giveLife(final Player player, final PlayerProfile targetProfile, final Integer amount) {
        if (targetProfile == null) {
            player.sendMessage(config.getMessage(ConfigMessage.UNKNOWN_PLAYER));
            return;
        }

        if (targetProfile.getOfflinePlayer().getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(config.getMessage(ConfigMessage.SELF_GIVE_LIFE));
            return;
        }

        PlayerProfile playerProfile = playerProfileManager.getByUUID(player.getUniqueId());

        try {
            if (targetProfile.getLife() > amount || (targetProfile.getLife() - amount) >= 1 || playerProfile == null) {
                player.sendMessage(config.getMessage(ConfigMessage.GIVE_LIFE_ERROR));
                return;
            }
        } catch (Exception exception) {
            player.sendMessage(config.getMessage(ConfigMessage.NUMBER_EXCEPTION));
            return;
        }

        playerProfile.removeLife(amount);
        targetProfile.addLife(amount);

        player.sendMessage(config.getMessage(ConfigMessage.GIVE_LIFE_SUCCESS).replace("%targetProfile%", targetProfile.getOfflinePlayer().getName()).replace("%number%", String.valueOf(amount)));
        if (targetProfile.getOfflinePlayer().isOnline()) {
            targetProfile.getOfflinePlayer().getPlayer().sendMessage(config.getMessage(ConfigMessage.GIVE_LIFE_SUCCESS_TO_TARGET).replace("%player%", player.getName()).replace("%number%", String.valueOf(amount)));
        }
    }

    @SubCommand("item")
    @Permission("life.command.item")
    public void item(final Player player) {
        player.getInventory().addItem(ItemUtil.getLifeAddItem(config, (int) config.getOption(ConfigOption.ADD_LIFE_ITEM_LIFE_TO_ADD)));
        player.sendMessage(config.getMessage(ConfigMessage.ITEM_ADD_LIFE_SUCCESS));
    }

    @SubCommand("reload")
    @Permission("life.command.reload")
    public void reload(final Player player) {
        config.reload();
        player.sendMessage(config.getMessage(ConfigMessage.RELOAD_MESSAGE));
    }
}
