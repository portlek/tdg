package io.github.portlek.tdg.api.events.abs;

import io.github.portlek.tdg.api.OpenedMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class MenuEvent extends TDGEvent {

    @NotNull
    private final OpenedMenu openedMenu;

    public MenuEvent(@NotNull Player who, @NotNull OpenedMenu openedMenu) {
        super(who);
        this.openedMenu = openedMenu;
    }

    @NotNull
    public OpenedMenu getOpenedMenu() {
        return openedMenu;
    }
}