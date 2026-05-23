package com.announcer.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.time.Duration;

public class AnnouncerCommand implements CommandExecutor {

    private final AnnouncerPlugin plugin;

    // Constructor correctly references our renamed main plugin class
    public AnnouncerCommand(AnnouncerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permission Check
        if (sender instanceof Player player) {
            if (!player.isOp()) {
                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cYou do not have permission to perform this command."));
                return true;
            }
        }

        // Check if arguments were provided
        if (args.length == 0) {
            sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cIncorrect usage! Please use: /announce <title> , <subtitle>"));
            return true;
        }

        // Reconstruct the full arguments string to easily find the comma separator
        String fullInput = String.join(" ", args);
        String titleText;
        String subtitleText = "";

        if (fullInput.contains(",")) {
            String[] parts = fullInput.split(",", 2);
            titleText = parts[0].trim();
            subtitleText = parts[1].trim();
        } else {
            titleText = fullInput.trim();
        }

        // Fetch timings from config.yml (Default to 20, 70, 20 ticks if missing)
        int fadeInTicks = plugin.getConfig().getInt("FadeIn", 20);
        int stayTicks = plugin.getConfig().getInt("Stay", 70);
        int fadeOutTicks = plugin.getConfig().getInt("FadeOut", 20);

        // Convert ticks to Milliseconds for the Adventure API (1 tick = 50ms)
        Duration fadeIn = Duration.ofMillis(fadeInTicks * 50L);
        Duration stay = Duration.ofMillis(stayTicks * 50L);
        Duration fadeOut = Duration.ofMillis(fadeOutTicks * 50L);

        // Build the modern text components
        Component mainTitle = LegacyComponentSerializer.legacyAmpersand().deserialize(titleText);
        Component subTitle = LegacyComponentSerializer.legacyAmpersand().deserialize(subtitleText);

        // Create the title object configuration
        Title.Times times = Title.Times.times(fadeIn, stay, fadeOut);
        Title title = Title.title(mainTitle, subTitle, times);

        // Send the title to everyone on the server natively
        plugin.getServer().showTitle(title);

        return true;
    }
}