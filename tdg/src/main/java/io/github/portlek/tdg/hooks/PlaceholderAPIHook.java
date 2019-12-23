package io.github.portlek.tdg.hooks;

import io.github.portlek.tdg.api.hook.Hook;
import io.github.portlek.tdg.api.hook.Wrapped;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class PlaceholderAPIHook implements Hook {

    private PlaceholderAPIPlugin placeholderAPI;

    @Override
    public boolean initiate() {
        return (placeholderAPI = (PlaceholderAPIPlugin) Bukkit.getPluginManager().getPlugin("PlaceholderAPI")) != null;
    }

    @NotNull
    public Wrapped create() {
        if (placeholderAPI == null) {
            throw new IllegalStateException("PlaceholderAPI not initiated! Use PlaceholderAPIHook#initiate method.");
        }

        return new PlaceholderAPIWrapper();
    }

}
