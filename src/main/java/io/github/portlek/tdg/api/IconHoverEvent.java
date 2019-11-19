package io.github.portlek.tdg.api;

import io.github.portlek.tdg.Icon;
import io.github.portlek.tdg.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public final class IconHoverEvent extends MenuEvent implements Cancellable {

    @NotNull
    private final Icon icon;

    private boolean cancelled;

    public IconHoverEvent(@NotNull Player who, @NotNull Menu menu, @NotNull Icon icon) {
        super(who, menu);
        this.icon = icon;
    }

    @NotNull
    public Icon getIcon() {
        return icon;
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
