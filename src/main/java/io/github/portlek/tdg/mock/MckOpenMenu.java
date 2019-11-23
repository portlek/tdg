package io.github.portlek.tdg.mock;

import io.github.portlek.tdg.LiveIcon;
import io.github.portlek.tdg.OpenedMenu;
import io.github.portlek.tdg.events.MenuCloseEvent;
import io.github.portlek.tdg.events.MenuOpenEvent;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class MckOpenMenu implements OpenedMenu {
    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }
    @Override
    public void addIcons(@NotNull List<LiveIcon> liveIcons) {
        throw new UnsupportedOperationException();
    }
    @Override
    public @NotNull LiveIcon findByEntity(@NotNull Entity entity) {
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
