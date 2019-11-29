package io.github.portlek.tdg.api;

import io.github.portlek.tdg.api.events.IconClickEvent;
import io.github.portlek.tdg.api.events.IconHoverEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public interface Icon extends Id, BiAcceptable<IconClickEvent, IconHoverEvent> {

    @NotNull
    LiveIcon createFor(@NotNull Player player, IntFunction<Location> function, boolean changed);

}
