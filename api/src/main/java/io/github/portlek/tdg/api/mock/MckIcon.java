package io.github.portlek.tdg.api.mock;

import io.github.portlek.tdg.api.Icon;
import io.github.portlek.tdg.api.LiveIcon;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public final class MckIcon implements Icon {
    @NotNull
    @Override
    public LiveIcon createFor(@NotNull Player player, IntFunction<Location> function,
                              boolean changed) {
        throw new UnsupportedOperationException();
    }
}
