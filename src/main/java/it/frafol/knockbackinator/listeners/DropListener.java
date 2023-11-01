package it.frafol.knockbackinator.listeners;

import it.frafol.knockbackinator.Knockbackinator;
import it.frafol.knockbackinator.enums.SpigotConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class DropListener implements Listener {

    private final Knockbackinator plugin = Knockbackinator.getInstance();

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {

        final ItemStack dropped = event.getItemDrop().getItemStack();

        if (dropped.getItemMeta() == null) {
            return;
        }

        if (!Objects.equals(dropped.getItemMeta().toString(), String.valueOf(plugin.getStick().getItemMeta()))) {
            return;
        }

        if (SpigotConfig.DROP.get(Boolean.class)) {
            event.setCancelled(true);
        }
    }
}
