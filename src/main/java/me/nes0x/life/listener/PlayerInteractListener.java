package me.nes0x.life.listener;

import me.nes0x.life.config.ConfigManager;
import me.nes0x.life.config.ConfigMessage;
import me.nes0x.life.config.ConfigOption;
import me.nes0x.life.profile.PlayerProfile;
import me.nes0x.life.profile.PlayerProfileManager;
import me.nes0x.life.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {
    private final PlayerProfileManager playerProfileManager;
    private final ConfigManager config;

    public PlayerInteractListener(final PlayerProfileManager playerProfileManager, final ConfigManager config) {
        this.playerProfileManager = playerProfileManager;
        this.config = config;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getInventory().getItemInMainHand().getType() != Material.AIR
                && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            int number = (int) config.getOption(ConfigOption.ADD_LIFE_ITEM_LIFE_TO_ADD);
            ItemStack item = ItemUtil.getLifeAddItem(config, number);
            if (player.getInventory().getItemInMainHand().getType() == item.getType()
                    &&
                player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(item.getItemMeta().getDisplayName())
            ) {
                if (!(boolean)config.getOption(ConfigOption.ADD_LIFE_ITEM_ENABLED)) {
                    return;
                }
                event.setCancelled(true);
                PlayerProfile profile = playerProfileManager.getByUUID(player.getUniqueId());
                if (profile == null) {
                    return;
                }
                profile.addLife(number);
                player.getInventory().removeItem(item);
                if ((boolean) config.getOption(ConfigOption.ADD_LIFE_ITEM_ENABLE_MESSAGE_ON_CLICK)) {
                    player.sendMessage(config.getMessage(ConfigMessage.MESSAGE_ON_CLICK)
                            .replace("%number%", String.valueOf(number)));
                }
            }
        }
    }
}
