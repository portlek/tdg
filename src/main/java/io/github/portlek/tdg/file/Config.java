package io.github.portlek.tdg.file;

import org.jetbrains.annotations.NotNull;

public final class Config {

    @NotNull
    public final String pluginPrefix;

    @NotNull
    public final String language;

    public final boolean hooksPlaceholderAPI;

    public Config(@NotNull String pluginPrefix, @NotNull String language, boolean hooksPlaceholderAPI) {
        this.pluginPrefix = pluginPrefix;
        this.language = language;
        this.hooksPlaceholderAPI = hooksPlaceholderAPI;
    }
}
