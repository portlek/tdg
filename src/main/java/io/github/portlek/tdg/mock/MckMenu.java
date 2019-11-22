package io.github.portlek.tdg.mock;

import io.github.portlek.tdg.Menu;
import io.github.portlek.tdg.events.MenuCloseEvent;
import io.github.portlek.tdg.events.MenuOpenEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class MckMenu implements Menu {
    @Override
    public boolean hasPermission(@NotNull Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean is(@NotNull String command) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void open(@NotNull Player player, boolean changed) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void accept(@NotNull MenuOpenEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void accept(@NotNull MenuCloseEvent event) {
        throw new UnsupportedOperationException();
    }
}
