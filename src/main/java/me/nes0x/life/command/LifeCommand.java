package me.nes0x.life.command;

import me.nes0x.life.Life;
import me.nes0x.life.util.ItemUtil;
import me.nes0x.life.util.LifeManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

import static me.nes0x.life.util.DisplayUtil.fixColors;

public class LifeCommand implements CommandExecutor {
    private final Life main;

    public LifeCommand(final Life main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = main.getConfig();
        OfflinePlayer target;
        LifeManager manager;

        // /life set <nick> <number>
        if (sender instanceof ConsoleCommandSender && args.length == 3 && args[0].equalsIgnoreCase("set")) {
            Logger logger = Bukkit.getLogger();
            target = Bukkit.getOfflinePlayer(args[1]);
            if (!target.hasPlayedBefore() && !target.isOnline()) {
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

        // /life
        if (args.length == 0) {
            manager = new LifeManager(player.getUniqueId(), main);
            player.sendMessage(fixColors(config.getString("messages.number-of-life").replace("%life%", String.valueOf(manager.getLife()))));
        // /life reload
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("life.commands.reload")) {
                player.sendMessage((fixColors(config.getString("messages.no-permission"))));
                return false;
            }
            main.reloadConfig();
            main.saveConfig();
            player.sendMessage(fixColors(config.getString("messages.reload-message")));
        // /life item
        } else if (args.length == 1 && args[0].equalsIgnoreCase("item"))  {
            if (!player.hasPermission("life.commands.item")) {
                player.sendMessage((fixColors(config.getString("messages.no-permission"))));
                return false;
            }

            player.getInventory().addItem(ItemUtil.getLifeAddItem(config, config.getInt("add-life-item.number")));
            player.sendMessage(fixColors(config.getString("messages.item-add-life-success")));
        // /life <nick>
        } else if (args.length == 1) {
            target = Bukkit.getOfflinePlayer(args[0]);
            if (!target.hasPlayedBefore() && !target.isOnline()) {
                player.sendMessage(fixColors(config.getString("messages.unknown-player")));
                return false;
            }
            manager = new LifeManager(target.getUniqueId(), main);
            player.sendMessage(fixColors(config.getString("messages.number-of-life-player").replace("%player%", target.getName()).replace("%life%", String.valueOf(manager.getLife()))));
        // /life <add/remove/set/give> <nick> <number>
        } else if (args.length == 3) {
            target = Bukkit.getOfflinePlayer(args[1]);
            if (!target.hasPlayedBefore() && !target.isOnline()) {
                player.sendMessage(fixColors(config.getString("messages.unknown-player")));
                return false;
            }
            manager = new LifeManager(target.getUniqueId(), main);
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
                case "give":
                    if (!player.hasPermission("life.commands.give")) {
                        player.sendMessage(fixColors(config.getString("messages.no-permission")));
                        return false;
                    }
                    if (target.getUniqueId().equals(player.getUniqueId())) {
                        player.sendMessage(fixColors(config.getString("messages.self-give-life")));
                        return false;
                    }

                    manager = new LifeManager(player.getUniqueId(), main);
                    if (manager.getLife() > number || (manager.getLife() - number) <= 1) {
                        manager.removeLife(number);
                        manager = new LifeManager(target.getUniqueId(), main);
                        manager.addLife(number);
                        player.sendMessage(fixColors(config.getString("messages.give-life-success").replace("%target%", args[1]).replace("%number%", String.valueOf(number))));
                        if (target.isOnline()) {
                            target.getPlayer().sendMessage(fixColors(config.getString("messages.give-life-success-to-target").replace("%player%", player.getName()).replace("%number%", String.valueOf(number))));
                        }
                    } else {
                        player.sendMessage(fixColors(config.getString("messages.give-life-error")));
                    }

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
