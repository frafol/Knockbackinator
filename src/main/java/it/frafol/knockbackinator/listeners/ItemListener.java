package it.frafol.knockbackinator.listeners;

import it.frafol.knockbackinator.Knockbackinator;
import it.frafol.knockbackinator.enums.SpigotConfig;
import it.frafol.knockbackinator.enums.SpigotMessages;
import it.frafol.knockbackinator.objects.PlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.Objects;

public class ItemListener implements Listener {

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent event) {

        Player player = event.getPlayer();

        if (player.getInventory().getItem(event.getNewSlot()) == null ) {
            if (player.getMaximumNoDamageTicks() != PlayerCache.getDelays().get(player)) {
                player.setMaximumNoDamageTicks(PlayerCache.getDelays().get(player));
            }
            return;
        }

        if (player.getInventory().getItem(event.getNewSlot()).getItemMeta() == null && player.getMaximumNoDamageTicks() != PlayerCache.getDelays().get(player)) {
            player.setMaximumNoDamageTicks(PlayerCache.getDelays().get(player));
            return;
        }

        if (Objects.equals(player.getInventory().getItem(event.getNewSlot()).getItemMeta().toString(), Knockbackinator.getInstance().getStick().getItemMeta().toString())) {

            if (!Objects.equals(SpigotMessages.TAKEN.get(String.class), "none")) {
                player.sendMessage(SpigotMessages.TAKEN.color().replace("%prefix%", SpigotMessages.PREFIX.color()));
            }

            player.setMaximumNoDamageTicks(SpigotConfig.HIT_DELAY.get(Integer.class));
            return;
        }

        if (player.getMaximumNoDamageTicks() != PlayerCache.getDelays().get(player)) {
            player.setMaximumNoDamageTicks(PlayerCache.getDelays().get(player));
        }

    }

    @EventHandler
    public void onItemMove(InventoryClickEvent event) {

        if (!SpigotConfig.MOVE.get(Boolean.class)) {
            return;
        }

        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getCurrentItem().getItemMeta() == null) {
            return;
        }

        if (event.getCurrentItem().getItemMeta().toString().equals(Knockbackinator.getInstance().getStick().getItemMeta().toString())) {
            event.setCancelled(true);
        }
    }
}
