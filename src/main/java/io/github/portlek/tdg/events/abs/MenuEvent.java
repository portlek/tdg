package io.github.portlek.tdg.events.abs;

import io.github.portlek.tdg.OpenedMenu;
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