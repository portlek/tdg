package io.github.portlek.tdg.mock;

import io.github.portlek.tdg.Icon;
import io.github.portlek.tdg.OpenedMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class MckOpenMenu implements OpenedMenu {
    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOpen() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull List<Icon> getIconsFor() {
        throw new UnsupportedOperationException();
    }

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
