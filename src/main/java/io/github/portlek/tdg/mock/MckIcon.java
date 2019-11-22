package io.github.portlek.tdg.mock;

import io.github.portlek.tdg.Icon;
import io.github.portlek.tdg.LiveIcon;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public final class MckIcon implements Icon {
    @NotNull
    @Override
    public LiveIcon createFor(@NotNull Player player, @NotNull BiFunction<Integer, Integer, Location> function,
                              boolean changed) {
        throw new UnsupportedOperationException();
    }
}
