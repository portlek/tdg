package io.github.portlek.tdg.api.mock;

import io.github.portlek.tdg.api.Icon;
import io.github.portlek.tdg.api.LiveIcon;
import io.github.portlek.tdg.api.events.IconClickEvent;
import io.github.portlek.tdg.api.events.IconHoverEvent;
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
    @NotNull
    @Override
    public String getId() {
        throw new UnsupportedOperationException();
    }
    @Override
    public void exec(IconHoverEvent event) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void accept(IconClickEvent event) {
        throw new UnsupportedOperationException();
    }
}
