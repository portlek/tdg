package io.github.portlek.tdg.api.events;

import io.github.portlek.tdg.api.LiveIcon;
import io.github.portlek.tdg.api.OpenedMenu;
import io.github.portlek.tdg.api.events.abs.IconEvent;
import io.github.portlek.tdg.api.type.ClickType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public final class IconClickEvent extends IconEvent implements Cancellable {

    @NotNull
    private final ClickType clickType;

    private boolean cancelled;

    public IconClickEvent(@NotNull Player who, @NotNull OpenedMenu openedMenu, @NotNull LiveIcon liveIcon,
                          @NotNull ClickType clickType) {
        super(who, openedMenu, liveIcon);
        this.clickType = clickType;
    }

    @NotNull
    public ClickType getClickType() {
        return clickType;
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