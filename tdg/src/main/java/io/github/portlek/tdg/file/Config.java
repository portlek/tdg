package io.github.portlek.tdg.file;

import io.github.portlek.tdg.hooks.VaultHook;
import org.jetbrains.annotations.NotNull;

public final class Config {

    @NotNull
    public final String language;

    public final boolean updateCheck;

    @NotNull
    public final String pluginPrefix;

    public final boolean hoverEffect;

    public final boolean hooksPlaceholderAPI;

    public final boolean hooksVault;

    public final VaultHook vault;

    public Config(@NotNull String language, boolean updateCheck, @NotNull String pluginPrefix, boolean hoverEffect,
                  boolean hooksPlaceholderAPI, boolean hooksVault, VaultHook vault) {
        this.language = language;
        this.updateCheck = updateCheck;
        this.pluginPrefix = pluginPrefix;
        this.hoverEffect = hoverEffect;
        this.hooksPlaceholderAPI = hooksPlaceholderAPI;
        this.hooksVault = hooksVault;
        this.vault = vault;
    }
}
