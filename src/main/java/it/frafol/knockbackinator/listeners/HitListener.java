package it.frafol.knockbackinator.listeners;

import it.frafol.knockbackinator.Knockbackinator;
import it.frafol.knockbackinator.enums.SpigotConfig;
import it.frafol.knockbackinator.objects.PlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Objects;

public class HitListener implements Listener {

    private Knockbackinator instance = Knockbackinator.getInstance();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();

        if (PlayerCache.getDelays().get(damaged) == null || PlayerCache.getDelays().get(damager) == null) {
            instance.getLogger().severe("A player tried to do something when the plugin is still loading.");
            event.setCancelled(true);
            return;
        }

        if (damager.getItemInHand().getItemMeta() == null || damaged.getItemInHand().getItemMeta() == null) {
            if (SpigotConfig.PREVENT_PVP.get(Boolean.class)) {
                event.setCancelled(true);
            }
            return;
        }

        if (!Objects.equals(damager.getItemInHand().getItemMeta().toString(), instance.getStick().getItemMeta().toString())) {
            if (SpigotConfig.PREVENT_PVP.get(Boolean.class)) {
                event.setCancelled(true);
            }
            return;
        }

        if (!Objects.equals(damaged.getItemInHand().getItemMeta().toString(), instance.getStick().getItemMeta().toString())) {
            if (SpigotConfig.PREVENT_PVP.get(Boolean.class)) {
                event.setCancelled(true);
            }
            return;
        }

        event.setCancelled(true);

        if (SpigotConfig.PREVENT_DAMAGE.get(Boolean.class)) {
            damaged.damage(1.0);
            damaged.setHealth(20.0);
        }

        damaged.setVelocity(damaged.getLocation().getDirection().normalize()
                .setX(Double.parseDouble(SpigotConfig.X.get(String.class)))
                .setY(Double.parseDouble(SpigotConfig.Y.get(String.class)))
                .setZ(Double.parseDouble(SpigotConfig.Z.get(String.class))));

        PlayerCache.getFall_time().put(damaged, 10);
    }
}
