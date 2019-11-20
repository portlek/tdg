package io.github.portlek.tdg.events;

import io.github.portlek.tdg.OpenedMenu;
import io.github.portlek.tdg.events.abs.MenuEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public class MenuOpenEvent extends MenuEvent implements Cancellable {

    private boolean cancelled;

    public MenuOpenEvent(@NotNull Player who, @NotNull OpenedMenu menu) {
        super(who, menu);
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