package io.github.portlek.tdg.menu;

import io.github.portlek.tdg.Icon;
import io.github.portlek.tdg.Menu;
import io.github.portlek.tdg.OpenedMenu;
import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.action.ActionBase;
import io.github.portlek.tdg.events.MenuCloseEvent;
import io.github.portlek.tdg.events.MenuOpenEvent;
import io.github.portlek.tdg.util.Metadata;
import io.github.portlek.tdg.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
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

    @Override
    public boolean hasPermission(@NotNull Player player) {
        return player.hasPermission("tdg.open." + id);
    }

    @Override
    public boolean is(@NotNull String command) {
        return commands.contains(command);
    }

    @Override
    public void open(@NotNull Player player, boolean changed) {
        final OpenedMenu openedMenu = new BasicOpenMenu(player, this);
        final MenuOpenEvent menuOpenEvent = new MenuOpenEvent(player, openedMenu);

        Bukkit.getServer().getPluginManager().callEvent(menuOpenEvent);

        if (menuOpenEvent.isCancelled()) {
            return;
        }

        for (Entity en : player.getWorld().getEntities()) {
            if (Metadata.hasKey(en, player.getName())) {
                en.remove();
                TDG.getAPI().entities.remove(en);
            }
        }

        final Location location;

        if (changed) {
            location = TDG.getAPI().lastLocations.get(player);
        } else {
            location = Utils.getBFLoc(player.getLocation(), 3.5);
            TDG.getAPI().lastLocations.put(player, location);
        }

        openedMenu.addIcons(
            new Mapped<>(
                icon -> icon.createFor(
                    player,
                    (positionX, positionY) -> Utils.setPosition(location, positionX, positionY, x1, x2, x4, x5),
                    changed
                ),
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
