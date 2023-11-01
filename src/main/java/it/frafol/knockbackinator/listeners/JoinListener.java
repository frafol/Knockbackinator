package it.frafol.knockbackinator.listeners;

import it.frafol.knockbackinator.Knockbackinator;
import it.frafol.knockbackinator.enums.SpigotConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final Knockbackinator plugin = Knockbackinator.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        plugin.startupPlayer(player);

        if (player.hasPermission(SpigotConfig.RELOAD_PERMISSION.get(String.class))) {
            plugin.UpdateChecker(player);
        }
    }
}
