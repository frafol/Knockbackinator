package it.frafol.knockbackinator.enums;

import it.frafol.knockbackinator.Knockbackinator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SpigotConfig {

    PERMISSION("settings.usage_permission"),
    RELOAD_PERMISSION("settings.reload_permission"),
    CREDIT_LESS("settings.credit_less"),
    SLOT("settings.item.slot"),
    ITEM_NAME("settings.item.name"),
    DROP("settings.item.prevent_drop"),
    MOVE("settings.item.prevent_move"),
    BREAK("settings.item.prevent_break"),
    DELAY("settings.item.delay"),
    LORE("settings.item.lore"),
    UPDATE_CHECK("settings.update_check"),
    AUTO_UPDATE("settings.auto_update"),
    PREVENT_PVP("settings.prevent_other_item_pvp"),
    PREVENT_DAMAGE("settings.prevent_taking_damage"),
    REMOVE_EFFECTS("settings.remove_all_effects"),
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
        String hex = convertHexColors(get(String.class));
        return hex.replace("&", "§");
    }

    private String convertHexColors(String message) {

        if (!containsHexColor(message)) {
            return message;
        }

        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return message;
    }

    private boolean containsHexColor(String message) {
        String hexColorPattern = "(?i)&#[a-f0-9]{6}";
        return message.matches(".*" + hexColorPattern + ".*");
    }

    public List<String> getStringList() {
        return instance.getConfigTextFile().getStringList(path);
    }

    public <T> T get(Class<T> clazz) {
        return clazz.cast(instance.getConfigTextFile().get(path));
    }

}