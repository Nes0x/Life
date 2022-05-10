package me.nes0x.life.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class LifeTabComplete implements TabCompleter {
    private final List<String> permissions =
            Arrays.asList(
                    "life.commands.add",
                    "life.commands.remove",
                    "life.commands.set",
                    "life.commands.reload",
                    "life.commands.item",
                    "life.commands.give"
            );

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String s, final String[] args) {
        List<String> completions = new ArrayList<>();
        Set<PermissionAttachmentInfo> playerPermissions = commandSender.getEffectivePermissions();
        if (args.length == 1) {
            if (commandSender.isOp()) {
                permissions.forEach(permission -> completions.add(permission.replace("life.commands.", "")));
            } else {

                permissions.forEach(permission -> {
                    playerPermissions.forEach(playerPermission ->  {
                        if (permission.equalsIgnoreCase(playerPermission.getPermission())) {
                            completions.add(playerPermission.getPermission().replace("life.commands.", ""));
                        }
                    });
                });
            }


            Bukkit.getOnlinePlayers().forEach(player -> completions.add(player.getName()));
            return StringUtil.copyPartialMatches(args[0], new ArrayList<>(), completions);
        } else if (args.length == 2) {
                boolean result = permissions.stream().anyMatch(permission -> {
                    if (commandSender.isOp()) {
                        return true;
                    }

                    for (PermissionAttachmentInfo playerPermission : playerPermissions) {
                        if (playerPermission.getPermission().equalsIgnoreCase(permission)) {
                            return true;
                        }
                    }
                    return false;
                });

                if (result) {
                    Bukkit.getOnlinePlayers().forEach(player -> completions.add(player.getName()));
                    return StringUtil.copyPartialMatches(args[1], new ArrayList<>(), completions);
                }
        }

        return new ArrayList<>();
    }
}
