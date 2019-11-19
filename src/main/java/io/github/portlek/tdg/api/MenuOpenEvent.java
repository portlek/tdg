package io.github.portlek.tdg.api;

import io.github.portlek.tdg.Menu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MenuOpenEvent extends MenuEvent {

    public MenuOpenEvent(@NotNull Player who, @NotNull Menu menu) {
        super(who, menu);
    }
}
