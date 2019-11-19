package io.github.portlek.tdg.menu;

import io.github.portlek.tdg.Icon;
import io.github.portlek.tdg.Menu;
import io.github.portlek.tdg.OpenedMenu;
import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.types.ActionType;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class BasicOpenMenu implements OpenedMenu {

    private List<Player> view = new ArrayList<>();

    private List<Player> toHide = new ArrayList<>();

    @NotNull
    private final Menu menu;

    @NotNull
    private final Player player;

    public BasicOpenMenu(@NotNull Menu menu, @NotNull Player player) {
        this.menu = menu;
        this.player = player;
    }

    @Override
    public void close() {
        getIconsFor().forEach(Icon::close);
        menu.acceptCloseEvent(player);
        TDG.getAPI().opened.remove(player.getUniqueId());
        view.remove(player);
    }

    @NotNull
    @Override
    public List<Icon> getIconsFor() {
        return new ListOf<>();
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

    @Override
    public void open(@NotNull Player player) {
        menu.open(player);
    }

}
