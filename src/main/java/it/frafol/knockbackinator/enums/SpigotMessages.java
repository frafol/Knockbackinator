package it.frafol.knockbackinator.enums;

import it.frafol.knockbackinator.Knockbackinator;

public enum SpigotMessages {

    PREFIX("messages.prefix"),
    TAKEN("messages.taken"),
    USAGE("messages.usage"),
    NO_PERMISSION("messages.no_permission"),
    RELOADED("messages.reloaded");

    private final String path;
    public static final Knockbackinator instance = Knockbackinator.getInstance();

    SpigotMessages(String path) {
        this.path = path;
    }

    public String color() {
        return get(String.class).replace("&", "§");
    }

    public <T> T get(Class<T> clazz) {
        return clazz.cast(instance.getMessagesTextFile().get(path));
    }

}
