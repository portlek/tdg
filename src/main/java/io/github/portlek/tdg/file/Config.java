package io.github.portlek.tdg.file;

import org.jetbrains.annotations.NotNull;

public final class Config {

    @NotNull
    public final String language;

    public final boolean updateCheck;

    public final int menuCooldown;

    @NotNull
    public final String pluginPrefix;

    public final boolean hooksPlaceholderAPI;

    public Config(@NotNull String language, boolean updateCheck, int menuCooldown, @NotNull String pluginPrefix, boolean hooksPlaceholderAPI) {
        this.language = language;
        this.updateCheck = updateCheck;
        this.menuCooldown = menuCooldown;
        this.pluginPrefix = pluginPrefix;
        this.hooksPlaceholderAPI = hooksPlaceholderAPI;
    }

}
