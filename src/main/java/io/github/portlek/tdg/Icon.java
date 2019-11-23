package io.github.portlek.tdg;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public interface Icon {

    @NotNull
    LiveIcon createFor(@NotNull Player player, IntFunction<Location> function,
                       boolean changed);

}
