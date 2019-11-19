package io.github.portlek.tdg.mock;

import io.github.portlek.tdg.Menu;
import io.github.portlek.tdg.OpenedMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class MckMenu implements Menu {
    @Override
    public @NotNull String getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<String> getCommands() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull OpenedMenu open(@NotNull Player player) {
        throw new UnsupportedOperationException();
    }
}
