package io.github.portlek.tdg.file;

import org.jetbrains.annotations.NotNull;

public final class Config {

    @NotNull
    public final String language;

    private final int menuCooldown;

    @NotNull
    public final String pluginPrefix;

    public final boolean hooksPlaceholderAPI;

    public Config(@NotNull String language, int menuCooldown, @NotNull String pluginPrefix, boolean hooksPlaceholderAPI) {
        this.language = language;
        this.menuCooldown = menuCooldown;
        this.pluginPrefix = pluginPrefix;
        this.hooksPlaceholderAPI = hooksPlaceholderAPI;
    }

}
