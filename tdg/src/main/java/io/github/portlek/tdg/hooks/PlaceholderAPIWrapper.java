package io.github.portlek.tdg.hooks;

import io.github.portlek.tdg.api.hook.Wrapped;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlaceholderAPIWrapper implements Wrapped {

    @NotNull
    public String apply(@NotNull Player player, @NotNull String string) {
        return PlaceholderAPI.setPlaceholders(player, string);
    }

}
