package me.nes0x.life.commands;

import me.nes0x.life.Main;
import me.nes0x.life.utils.DisplayUtils;
import me.nes0x.life.utils.ItemUtils;
import me.nes0x.life.utils.LifeManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

import static me.nes0x.life.utils.DisplayUtils.fixColors;

public class Life implements CommandExecutor {
    private final Main main;

    public Life(final Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = main.getConfig();
        OfflinePlayer target;
        LifeManager manager;

        if (sender instanceof ConsoleCommandSender && args.length == 3 && args[0].equalsIgnoreCase("set")) {
            Logger logger = Bukkit.getLogger();
            if (!Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
                logger.info(fixColors(config.getString("messages.unknown-player")));
                return true;
            }

            int number;

            try {
                number = Integer.parseInt(args[2]);
            }  catch (NumberFormatException exception) {
                logger.info(fixColors(config.getString("messages.number-exception")));
                return false;
            }

            if (number < 0) {
                logger.info(fixColors(config.getString("messages.life-number-exception")));
                return false;
            }

            target = Bukkit.getOfflinePlayer(args[1]);
            manager = new LifeManager(target.getUniqueId(), main);
            manager.setLife(number);
            logger.info(fixColors(config.getString("messages.set-life-success").replace("%player%", target.getName()).replace("%number%", String.valueOf(number))));
            return true;
        }

        if (!(sender instanceof Player)) {
            Bukkit.getLogger().info(fixColors(config.getString("messages.command-in-console")));
            return true;
        }


        Player player = (Player) sender;


        if (args.length == 0) {
            manager = new LifeManager(player.getUniqueId(), main);
            player.sendMessage(fixColors(config.getString("messages.number-of-life").replace("%life%", String.valueOf(manager.getLife()))));
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("life.commands.reload")) {
                player.sendMessage((fixColors(config.getString("messages.no-permission"))));
                return false;
            }
            main.reloadConfig();
            main.saveConfig();
            player.sendMessage(fixColors(config.getString("messages.reload-message")));
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("item"))  {
            if (!player.hasPermission("life.commands.item")) {
                player.sendMessage((fixColors(config.getString("messages.no-permission"))));
                return false;
            }

            player.getInventory().addItem(ItemUtils.getLifeAddItem(config, config.getInt("add-life-item.number")));
            player.sendMessage(fixColors(config.getString("messages.item-add-life-success")));
        } else if (args.length == 1) {

            if (Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
                target = Bukkit.getOfflinePlayer(args[0]);
                manager = new LifeManager(target.getUniqueId(), main);
                player.sendMessage(fixColors(config.getString("messages.number-of-life-player").replace("%player%", target.getName()).replace("%life%", String.valueOf(manager.getLife()))));
            } else {
                player.sendMessage(fixColors(config.getString("messages.unknown-player")));
            }
        } else if (args.length == 3) {

            if (Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
                target = Bukkit.getOfflinePlayer(args[1]);
                manager = new LifeManager(target.getUniqueId(), main);
            } else {
                player.sendMessage(fixColors(config.getString("messages.unknown-player")));
                return false;
            }

            int number;

            try {
                number = Integer.parseInt(args[2]);
            }  catch (NumberFormatException exception) {
                player.sendMessage(fixColors(config.getString("messages.number-exception")));
                return false;
            }

            if (number < 0) {
                player.sendMessage(fixColors(config.getString("messages.life-number-exception")));
                return false;
            }


            switch (args[0].toLowerCase()) {
                case "add":
                    if (!player.hasPermission("life.commands.add")) {
                        player.sendMessage(fixColors(config.getString("messages.no-permission")));
                        return false;
                    }
                    manager.addLife(number);
                    player.sendMessage(fixColors(config.getString("messages.add-life-success").replace("%player%", target.getName()).replace("%number%", String.valueOf(number))));
                    break;
                case "remove":
                    if (!player.hasPermission("life.commands.remove")) {
                        player.sendMessage(fixColors(config.getString("messages.no-permission")));
                        return false;
                    }
                    if ((manager.getLife() - number) < 0) number = manager.getLife();
                    manager.removeLife(number);
                    player.sendMessage(fixColors(config.getString("messages.remove-life-success").replace("%player%", target.getName()).replace("%number%", String.valueOf(number))));
                    break;
                case "set":
                    if (!player.hasPermission("life.commands.set")) {
                        player.sendMessage(fixColors(config.getString("messages.no-permission")));
                        return false;
                    }
                    manager.setLife(number);
                    player.sendMessage(fixColors(config.getString("messages.set-life-success").replace("%player%", target.getName()).replace("%number%", String.valueOf(number))));
                    break;
                default:
                    player.sendMessage(fixColors(config.getString("messages.invalid-usage")));
                    return false;
            }
        } else {
            player.sendMessage(fixColors(config.getString("messages.invalid-usage")));
        }


        return true;
    }
}
