package me.nes0x.life.util;

import me.nes0x.life.Life;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

// From: https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates
public class UpdateUtil {
    private final Life main;
    private final int resourceId;

    public UpdateUtil(Life main, int resourceId) {
        this.main = main;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                main.getLogger().info(ChatColor.RED + "Cannnot c: " + exception.getMessage());
            }
        });
    }
}
