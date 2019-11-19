package io.github.portlek.tdg.api;

import io.github.portlek.tdg.Menu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class MenuEvent extends TDGEvent {

    @NotNull
    private final Menu menu;

    public MenuEvent(@NotNull Player who, @NotNull Menu menu) {
        super(who);
        this.menu = menu;
    }

    @NotNull
    public Menu getMenu() {
        return menu;
    }
}
