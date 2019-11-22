package io.github.portlek.tdg.mock;

import io.github.portlek.tdg.LiveIcon;
import io.github.portlek.tdg.OpenedMenu;
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
}
