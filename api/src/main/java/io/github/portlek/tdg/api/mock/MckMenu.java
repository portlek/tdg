package io.github.portlek.tdg.api.mock;

import io.github.portlek.tdg.api.Menu;
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
    @NotNull
    @Override
    public String getId() {
        throw new UnsupportedOperationException();
    }
}
