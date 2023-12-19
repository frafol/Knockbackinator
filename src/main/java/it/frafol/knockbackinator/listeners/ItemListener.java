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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class ItemListener implements Listener {

    private final Knockbackinator instance = Knockbackinator.getInstance();
    private final HashMap<UUID, List<PotionEffect>> effects = new HashMap<>();

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent event) {

        Player player = event.getPlayer();

        if (player.getInventory().getItem(event.getPreviousSlot()) != null && player.getInventory().getItem(event.getPreviousSlot()).equals(instance.getStick())) {
            if (SpigotConfig.REMOVE_EFFECTS.get(Boolean.class)) {
                effects.get(player.getUniqueId()).forEach(player::addPotionEffect);
                effects.remove(player.getUniqueId());
            }
        }

        if (player.getInventory().getItem(event.getNewSlot()) == null) {
            if (player.getMaximumNoDamageTicks() != PlayerCache.getDelays().get(player)) {
                player.setMaximumNoDamageTicks(PlayerCache.getDelays().get(player));
            }
            return;
        }

        if (player.getInventory().getItem(event.getNewSlot()).getItemMeta() == null && player.getMaximumNoDamageTicks() != PlayerCache.getDelays().get(player)) {
            player.setMaximumNoDamageTicks(PlayerCache.getDelays().get(player));
            return;
        }

        if (Objects.equals(player.getInventory().getItem(event.getNewSlot()).getItemMeta().toString(), instance.getStick().getItemMeta().toString())) {

            if (!Objects.equals(SpigotMessages.TAKEN.get(String.class), "none")) {
                player.sendMessage(SpigotMessages.TAKEN.color().replace("%prefix%", SpigotMessages.PREFIX.color()));
            }

            if (SpigotConfig.REMOVE_EFFECTS.get(Boolean.class)) {
                List<PotionEffect> potionEffectTypes = new ArrayList<>(player.getActivePotionEffects());
                potionEffectTypes.forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
                effects.put(player.getUniqueId(), potionEffectTypes);
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

        if (event.getCurrentItem().getItemMeta().toString().equals(instance.getStick().getItemMeta().toString())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        effects.remove(event.getPlayer().getUniqueId());
    }
}
