package it.frafol.knockbackinator.enums;

import it.frafol.knockbackinator.Knockbackinator;

public enum SpigotConfig {

    PERMISSION("settings.usage_permission"),
    RELOAD_PERMISSION("settings.reload_permission"),
    SLOT("settings.item.slot"),
    ITEM_NAME("settings.item.name"),
    DROP("settings.item.prevent_drop"),
    MOVE("settings.item.prevent_move"),
    BREAK("settings.item.prevent_break"),
    UPDATE_CHECK("settings.update_check"),
    PREVENT_PVP("settings.prevent_other_item_pvp"),
    PREVENT_DAMAGE("settings.prevent_taking_damage"),
    HIT_DELAY("settings.knockback.hit_delay"),
    X("settings.knockback.x_increment"),
    Y("settings.knockback.y_increment"),
    Z("settings.knockback.z_increment"),
    STATS("settings.stats");

    private final String path;
    public static final Knockbackinator instance = Knockbackinator.getInstance();

    SpigotConfig(String path) {
        this.path = path;
    }

    public String color() {
        return get(String.class).replace("&", "§");
    }

    public <T> T get(Class<T> clazz) {
        return clazz.cast(instance.getConfigTextFile().get(path));
    }

}