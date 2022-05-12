package me.nes0x.life.listener;

import me.nes0x.life.Life;
import me.nes0x.life.util.ItemUtil;
import me.nes0x.life.util.LifeManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static me.nes0x.life.util.DisplayUtil.fixColors;

public class PlayerInteractListener implements Listener {
    private final Life main;

    public PlayerInteractListener(final Life main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        FileConfiguration config = main.getConfig();
        if (config.getBoolean("add-life-item.enabled")) {
            Player player = event.getPlayer();
            if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {

                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    int number = config.getInt("add-life-item.life-to-add");
                    ItemStack item = ItemUtil.getLifeAddItem(config, number);
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
