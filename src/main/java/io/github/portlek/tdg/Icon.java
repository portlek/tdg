package io.github.portlek.tdg;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public interface Icon {

    @NotNull
    LiveIcon createFor(@NotNull Player player, @NotNull BiFunction<Integer, Integer, Location> function,
                       boolean changed);

}
