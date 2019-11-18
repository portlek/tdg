package io.github.portlek.tdg.mock;

import io.github.portlek.tdg.OpenedMenu;

public final class MckOpenMenu implements OpenedMenu {
    @Override
    public void open() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOpen() {
        throw new UnsupportedOperationException();
    }
}
