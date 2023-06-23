package it.frafol.knockbackinator.enums;

import it.frafol.knockbackinator.Knockbackinator;

public enum SpigotVersion {

    VERSION("version");

    private final String path;
    public static final Knockbackinator instance = Knockbackinator.getInstance();

    SpigotVersion(String path) {
        this.path = path;
    }

    public String color() {
        return get(String.class).replace("&", "§");
    }

    public <T> T get(Class<T> clazz) {
        return clazz.cast(instance.getVersionTextFile().get(path));
    }

}