package io.github.portlek.tdg.api.mock;

import io.github.portlek.tdg.api.LiveIcon;
import io.github.portlek.tdg.api.Menu;
import io.github.portlek.tdg.api.OpenedMenu;
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
    @NotNull
    @Override
    public Menu getParent() {
        throw new UnsupportedOperationException();
    }
    @NotNull
    @Override
    public List<LiveIcon> getLiveIcons() {
        throw new UnsupportedOperationException();
    }
}
