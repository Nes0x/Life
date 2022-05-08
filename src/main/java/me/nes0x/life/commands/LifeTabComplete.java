package me.nes0x.life.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class LifeTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String s, final String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if (commandSender.hasPermission("life.commands.add")) {
                completions.add("add");
            }
            if (commandSender.hasPermission("life.commands.remove")) {
                completions.add("remove");
            }
            if (commandSender.hasPermission("life.commands.set")) {
                completions.add("set");
            }
            if (commandSender.hasPermission("life.commands.reload")) {
                completions.add("reload");
            }
            Bukkit.getOnlinePlayers().forEach(player -> completions.add(player.getName()));
            return StringUtil.copyPartialMatches(args[0], new ArrayList<>(), completions);
        } else if (args.length == 2) {
            if (commandSender.hasPermission("life.command.add") || commandSender.hasPermission("life.command.remove")) {
                Bukkit.getOnlinePlayers().forEach(player -> completions.add(player.getName()));
                return StringUtil.copyPartialMatches(args[1], new ArrayList<>(), completions);
            }
        }

        return new ArrayList<>();
    }
}
