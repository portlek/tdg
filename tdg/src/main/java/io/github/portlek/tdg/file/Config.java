package io.github.portlek.tdg.file;

import io.github.portlek.tdg.api.hook.Wrapped;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public final class Config {

    @NotNull
    public final String pluginLanguage;

    @NotNull
    public final String pluginPrefix;

    public final boolean checkForUpdate;

    public final boolean hoverEffect;

    @NotNull
    private final Map<String, Wrapped> wrapped;

    public Config(@NotNull String pluginLanguage, @NotNull String pluginPrefix, boolean checkForUpdate,
                  boolean hoverEffect, @NotNull Map<String, Wrapped> wrapped) {
        this.pluginLanguage = pluginLanguage;
        this.pluginPrefix = pluginPrefix;
        this.checkForUpdate = checkForUpdate;
        this.hoverEffect = hoverEffect;
        this.wrapped = wrapped;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public <T extends Wrapped> Optional<T> getWrapped(@NotNull String wrappedId) {
        return Optional.ofNullable(
            (T) wrapped.get(wrappedId)
        );
    }

}
