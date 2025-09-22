package it.frafol.knockbackinator.listeners;

import it.frafol.knockbackinator.Knockbackinator;
import it.frafol.knockbackinator.enums.SpigotConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;

public class EffectListener implements Listener {

    private final Knockbackinator plugin = Knockbackinator.getInstance();

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!player.getItemInHand().equals(plugin.getStick())) return;
        if (SpigotConfig.REMOVE_EFFECTS.get(Boolean.class)) event.setCancelled(true);
    }
}
