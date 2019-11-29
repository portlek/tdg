package io.github.portlek.tdg.api.events;

import io.github.portlek.tdg.api.OpenedMenu;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public final class MenuOpenEvent extends MenuEvent implements Cancellable {

    private final boolean changed;

    private boolean cancelled;

    public MenuOpenEvent(@NotNull Player who, @NotNull OpenedMenu openedMenu, boolean changed) {
        super(who, openedMenu);
        this.changed = changed;
    }

    public boolean isChanged() {
        return changed;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}