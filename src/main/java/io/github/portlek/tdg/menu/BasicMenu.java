package io.github.portlek.tdg.menu;

import io.github.portlek.tdg.*;
import io.github.portlek.tdg.events.MenuCloseEvent;
import io.github.portlek.tdg.events.MenuOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class BasicMenu implements Menu {

    @NotNull
    private final String id;

    @NotNull
    private final List<String> commands;

    @NotNull
    private final List<ActionBase<MenuCloseEvent>> closeAction;

    @NotNull
    private final List<ActionBase<MenuOpenEvent>> openAction;

    private final int x1;

    private final int x2;

    private final int x4;

    private final int x5;

    @NotNull
    private final List<Icon> icons;

    public BasicMenu(@NotNull String id, @NotNull List<String> commands,
                     @NotNull List<ActionBase<MenuCloseEvent>> closeAction,
                     @NotNull List<ActionBase<MenuOpenEvent>> openAction, int x1, int x2, int x4, int x5,
                     @NotNull List<Icon> icons) {
        this.id = id;
        this.commands = commands;
        this.closeAction = closeAction;
        this.openAction = openAction;
        this.x1 = x1;
        this.x2 = x2;
        this.x4 = x4;
        this.x5 = x5;
        this.icons = icons;
    }

    @NotNull
    @Override
    public String getId() {
        return id;
    }

    @NotNull
    @Override
    public List<String> getCommands() {
        return commands;
    }

    @NotNull
    @Override
    public List<Icon> getIcons() {
        return icons;
    }

    @Override
    public void open(@NotNull Player player) {
        final OpenedMenu openedMenu = new BasicOpenMenu(player, this);
        final MenuOpenEvent menuOpenEvent = new MenuOpenEvent(player, openedMenu);

        Bukkit.getServer().getPluginManager().callEvent(menuOpenEvent);

        if (menuOpenEvent.isCancelled()) {
            return;
        }

        openedMenu.addIcons(
            new Mapped<>(
                icon -> icon.createFor(player),
                icons
            )
        );
        accept(menuOpenEvent);
        TDG.getAPI().opened.put(player.getUniqueId(), openedMenu);
    }

    @Override
    public void accept(@NotNull MenuOpenEvent event) {
        openAction.forEach(action -> action.apply(event));
    }

    @Override
    public void accept(@NotNull MenuCloseEvent event) {
        closeAction.forEach(action -> action.apply(event));
    }
}
