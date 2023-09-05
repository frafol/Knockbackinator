package it.frafol.knockbackinator.commands;

import it.frafol.knockbackinator.enums.SpigotConfig;
import it.frafol.knockbackinator.enums.SpigotMessages;
import it.frafol.knockbackinator.objects.TextFile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class MainCommand implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {

        if (!event.getMessage().startsWith("/")) {
            return;
        }

        final String command = event.getMessage();
        final Player player = event.getPlayer();

        if (command.equals("/knockbackinator")) {

            event.setCancelled(true);

            if (player.hasPermission(SpigotConfig.RELOAD_PERMISSION.get(String.class))) {
                player.sendMessage(SpigotMessages.USAGE.color().replace("%prefix%", SpigotMessages.PREFIX.color()));
                return;
            }

            if (SpigotConfig.CREDIT_LESS.get(Boolean.class)) {
                player.sendMessage(SpigotMessages.NO_PERMISSION.color().replace("%prefix%", SpigotMessages.PREFIX.color()));
                return;
            }

            player.sendMessage("§7This server is using §dKnockbackinator §7by §dfrafol§7.");
        }

        if (command.equals("/knockbackinator reload")) {

            event.setCancelled(true);

            if (player.hasPermission(SpigotConfig.RELOAD_PERMISSION.get(String.class))) {
                TextFile.reloadAll();
                player.sendMessage(SpigotMessages.RELOADED.color().replace("%prefix%", SpigotMessages.PREFIX.color()));
                return;
            }

            if (SpigotConfig.CREDIT_LESS.get(Boolean.class)) {
                player.sendMessage(SpigotMessages.NO_PERMISSION.color().replace("%prefix%", SpigotMessages.PREFIX.color()));
                return;
            }

            player.sendMessage("§7This server is using §dKnockbackinator §7by §dfrafol§7.");
        }
    }
}