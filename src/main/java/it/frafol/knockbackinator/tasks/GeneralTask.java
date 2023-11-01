package it.frafol.knockbackinator.tasks;

import it.frafol.knockbackinator.Knockbackinator;
import it.frafol.knockbackinator.objects.PlayerCache;

public class GeneralTask implements Runnable {

    private final Knockbackinator plugin = Knockbackinator.getInstance();

    @Override
    public void run() {

        if (plugin.getServer().getOnlinePlayers().isEmpty()) {
            return;
        }

        plugin.getServer().getOnlinePlayers().forEach(players -> {

            if (PlayerCache.getFall_time().get(players) == null) {
                return;
            }

            if (PlayerCache.getFall_time().get(players) == 0) {
                PlayerCache.getFall_time().remove(players);
                return;
            }

            PlayerCache.getFall_time().put(players, PlayerCache.getFall_time().get(players) - 1);
        });
    }
}
