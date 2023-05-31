package it.frafol.knockbackinator.tasks;

import it.frafol.knockbackinator.Knockbackinator;
import it.frafol.knockbackinator.objects.PlayerCache;
import org.bukkit.entity.Player;

public class GeneralTask implements Runnable {

    @Override
    public void run() {

        if (Knockbackinator.getInstance().getServer().getOnlinePlayers() == null) {
            return;
        }

        for (Player players : Knockbackinator.getInstance().getServer().getOnlinePlayers()) {

            if (PlayerCache.getFall_time().get(players) == null) {
                return;
            }

            if (PlayerCache.getFall_time().get(players) == 0) {
                PlayerCache.getFall_time().remove(players);
                return;
            }

            PlayerCache.getFall_time().put(players, PlayerCache.getFall_time().get(players) - 1);

        }
    }
}
