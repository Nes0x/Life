package me.nes0x.life.util;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static me.nes0x.life.util.DisplayUtil.fixColors;

public class ItemUtil {

    public static ItemStack getLifeAddItem(FileConfiguration config, int number) {
        ItemStack item = new ItemStack(Material.valueOf(config.getString("add-life-item.material")), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(fixColors(config.getString("add-life-item.name").replace("%number%", String.valueOf(number))));
        List<String> lore = new ArrayList<>();
        config.getStringList("add-life-item.lore").forEach(line -> lore.add(fixColors(line.replace("%number%", String.valueOf(number)))));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}
