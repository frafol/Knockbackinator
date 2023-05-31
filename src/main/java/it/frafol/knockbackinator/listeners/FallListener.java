package it.frafol.knockbackinator.listeners;

import it.frafol.knockbackinator.objects.PlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FallListener implements Listener {

    @EventHandler
    public void onPlayerFall(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        final Player player = (Player) event.getEntity();

        if (PlayerCache.getFall_time().get(player) == null) {
            return;
        }

        event.setCancelled(true);

    }
}
