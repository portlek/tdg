package io.github.portlek.tdg.api;

import io.github.portlek.tdg.OpenedMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class MenuEvent extends TDGEvent {

    @NotNull
    private final OpenedMenu menu;

    public MenuEvent(@NotNull Player who, @NotNull OpenedMenu menu) {
        super(who);
        this.menu = menu;
    }

    @NotNull
    public OpenedMenu getMenu() {
        return menu;
    }
}
