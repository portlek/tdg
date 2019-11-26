package io.github.portlek.tdg.api.events;

import io.github.portlek.tdg.api.LiveIcon;
import io.github.portlek.tdg.api.OpenedMenu;
import io.github.portlek.tdg.api.events.abs.IconEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public final class IconHoverEvent extends IconEvent implements Cancellable {

    private boolean cancelled;

    public IconHoverEvent(@NotNull Player who, @NotNull OpenedMenu menu, @NotNull LiveIcon liveIcon) {
        super(who, menu, liveIcon);
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