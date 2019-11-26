package io.github.portlek.tdg.api.mock;

import io.github.portlek.tdg.api.LiveIcon;
import io.github.portlek.tdg.api.events.IconClickEvent;
import io.github.portlek.tdg.api.events.IconHoverEvent;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public final class MckLiveIcon implements LiveIcon {
    @Override
    public boolean is(@NotNull Entity entity) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void accept(@NotNull IconClickEvent event) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void accept(@NotNull IconHoverEvent event) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }
}
