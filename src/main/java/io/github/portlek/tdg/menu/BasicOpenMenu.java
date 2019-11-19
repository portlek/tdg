package io.github.portlek.tdg.menu;

import io.github.portlek.tdg.Icon;
import io.github.portlek.tdg.Menu;
import io.github.portlek.tdg.OpenedMenu;
import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.api.MenuCloseEvent;
import io.github.portlek.tdg.api.MenuOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
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

    @Override
    public void close() {
        final MenuCloseEvent menuCloseEvent = new MenuCloseEvent(player, this);

        Bukkit.getServer().getPluginManager().callEvent(menuCloseEvent);

        if (menuCloseEvent.isCancelled()) {
            return;
        }

        getIconsFor().forEach(Icon::close);
        accept(menuCloseEvent);
        TDG.getAPI().opened.remove(player.getUniqueId());
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

    @Override
    public void accept(@NotNull MenuCloseEvent event) {
        menu.accept(event);
    }

    @Override
    public void accept(@NotNull MenuOpenEvent event) {
        menu.accept(event);
    }
}
