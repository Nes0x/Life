package me.nes0x.life.listeners;


import me.nes0x.life.utils.LifeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import me.nes0x.life.Main;




public class PlayerDeath implements Listener {
    private final Main main;

    public PlayerDeath(final Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        LifeManager manager = new LifeManager(player.getUniqueId(), main);
        manager.removeLife(1);
    }

}
