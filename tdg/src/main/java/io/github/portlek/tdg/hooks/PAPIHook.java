package io.github.portlek.tdg.hooks;

import io.github.portlek.tdg.api.Hook;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class PAPIHook implements Hook<PlaceholderAPIPlugin> {

    private static PlaceholderAPIPlugin papi;

    @Override
    public boolean initiate() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papi = PlaceholderAPIPlugin.getInstance();
        }

        return papi != null;
    }

    @NotNull
    @Override
    public PlaceholderAPIPlugin get() {
        if (papi == null) {
            throw new RuntimeException("PlaceholderAPI not initiated! Use PAPIHook#initiate() method.");
        }

        return papi;
    }

}
