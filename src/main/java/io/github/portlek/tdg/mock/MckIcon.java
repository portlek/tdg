package io.github.portlek.tdg.mock;

import io.github.portlek.tdg.Icon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class MckIcon implements Icon {
    @Override
    public boolean is(@NotNull Entity entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playSound(@NotNull Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void acceptClickEvent(@NotNull Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void acceptHoverEvent(@NotNull Player player) {
        throw new UnsupportedOperationException();
    }
}
