package it.frafol.knockbackinator.tasks;

import it.frafol.knockbackinator.Knockbackinator;
import it.frafol.knockbackinator.objects.PlayerCache;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class FoliaTask {

    private ScheduledExecutorService scheduler;

    public void startTask() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        foliaTask();
    }

    private void startTask(Runnable task) {
        scheduler.scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS);
    }

    public void stopTask() {
        if (scheduler == null || scheduler.isShutdown()) {
            return;
        }
        scheduler.shutdown();
    }

    private void foliaTask() {
        Runnable task = () -> {
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
        };
        startTask(task);
    }
}
