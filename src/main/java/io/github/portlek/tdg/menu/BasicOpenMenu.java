package io.github.portlek.tdg.menu;

import io.github.portlek.tdg.Icon;
import io.github.portlek.tdg.Menu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class BasicOpenMenu implements OpenedMenu {

    @NotNull
    private final Menu menu;

    @NotNull
    private final Player player;

    public BasicOpenMenu(@NotNull Menu menu, @NotNull Player player) {
        this.menu = menu;
        this.player = player;
    }

    @NotNull
    @Override
    public List<Icon> getIconsFor() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @NotNull
    @Override
    public String getId() {
        return menu.getId();
    }

    @NotNull
    @Override
    public List<String> getCommands() {
        return menu.getCommands();
    }

    @NotNull
    @Override
    public OpenedMenu open(@NotNull Player player) {
        return menu.open(player);
    }

}
