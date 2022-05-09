package me.nes0x.life.listeners;

import me.nes0x.life.Main;
import me.nes0x.life.utils.ItemUtils;
import me.nes0x.life.utils.LifeManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static me.nes0x.life.utils.DisplayUtils.fixColors;

public class PlayerInteract implements Listener {
    private final Main main;

    public PlayerInteract(final Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        FileConfiguration config = main.getConfig();
        if (config.getBoolean("add-life-item.enabled")) {
            Player player = event.getPlayer();
            if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {

                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    int number = config.getInt("add-life-item.number");
                    ItemStack item = ItemUtils.getLifeAddItem(config, number);
                    if (player.getInventory().getItemInMainHand().getType() == item.getType()
                            &&
                        player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(item.getItemMeta().getDisplayName())
                    ) {
                        event.setCancelled(true);
                        LifeManager manager = new LifeManager(player.getUniqueId(), main);
                        manager.addLife(number);
                        player.getInventory().removeItem(item);
                        if (config.getBoolean("add-life-item.enable-message-on-click")) {
                            player.sendMessage(fixColors(config.getString("messages.message-on-click").replace("%number%", String.valueOf(number))));
                        }
                    }
                }
            }
        }


    }



}
