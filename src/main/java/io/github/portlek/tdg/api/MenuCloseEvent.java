package io.github.portlek.tdg.api;

import io.github.portlek.tdg.OpenedMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public class MenuCloseEvent extends MenuEvent implements Cancellable {

    private boolean cancelled;

    public MenuCloseEvent(@NotNull Player who, @NotNull OpenedMenu menu) {
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
