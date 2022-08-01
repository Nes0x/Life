package me.nes0x.life.util;

import me.nes0x.life.config.ConfigManager;
import me.nes0x.life.config.ConfigOption;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class ItemUtil {

    public static ItemStack getLifeAddItem(ConfigManager config, int number) {
        ItemStack item = new ItemStack(Material.valueOf(config.getOption(ConfigOption.ADD_LIFE_ITEM_MATERIAL).toString().toUpperCase()), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getOption(ConfigOption.ADD_LIFE_ITEM_NAME).toString())
                .replace("%number%", String.valueOf(number)));
        List<String> lore = new ArrayList<>();
        List<String> loreConfig = (List<String>) config.getOption(ConfigOption.ADD_LIFE_ITEM_LORE);
        loreConfig.forEach(line -> lore.add(
                ChatColor.translateAlternateColorCodes('&', line
                .replace("%number%", String.valueOf(number)))));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
