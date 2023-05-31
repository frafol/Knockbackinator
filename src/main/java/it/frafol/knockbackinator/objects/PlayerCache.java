package it.frafol.knockbackinator.objects;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.HashMap;

@UtilityClass
public class PlayerCache {

    @Getter
    private final HashMap<Player, Integer> delays = new HashMap<>();

    @Getter
    private final HashMap<Player, Integer> fall_time = new HashMap<>();

}