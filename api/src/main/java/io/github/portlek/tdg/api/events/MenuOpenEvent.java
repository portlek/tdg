package io.github.portlek.tdg.api.events;

import io.github.portlek.tdg.api.OpenedMenu;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public final class MenuOpenEvent extends MenuEvent implements Cancellable {

    private boolean cancelled;

    public MenuOpenEvent(@NotNull Player who, @NotNull OpenedMenu openedMenu) {
        super(who, openedMenu);
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