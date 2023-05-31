package it.frafol.knockbackinator.listeners;

import it.frafol.knockbackinator.objects.PlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {

        final Player player = event.getPlayer();

        PlayerCache.getDelays().remove(player);
        PlayerCache.getFall_time().remove(player);

    }
}
