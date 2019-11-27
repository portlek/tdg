package io.github.portlek.tdg.menu;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.api.*;
import io.github.portlek.tdg.api.events.MenuCloseEvent;
import io.github.portlek.tdg.api.events.MenuOpenEvent;
import io.github.portlek.tdg.api.mock.MckOpenMenu;
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
    private final List<Target<MenuCloseEvent>> closeTargets;

    @NotNull
    private final List<Target<MenuOpenEvent>> openTargets;

    private final int x1;

    private final int x2;

    private final int x4;

    private final int x5;

    @NotNull
    private final List<Icon> icons;

    public BasicMenu(@NotNull String id, @NotNull List<String> commands,
                     @NotNull List<Target<MenuCloseEvent>> closeTargets,
                     @NotNull List<Target<MenuOpenEvent>> openTargets, int x1, int x2, int x4, int x5,
                     @NotNull List<Icon> icons) {
        this.id = id;
        this.commands = commands;
        this.closeTargets = closeTargets;
        this.openTargets = openTargets;
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
        final OpenedMenu oldMenu = TDG.getAPI().opened.getOrDefault(player.getUniqueId(), new MckOpenMenu());

        if (!(oldMenu instanceof MckOpenMenu)) {
            oldMenu.close();
        }

        final OpenedMenu openedMenu = new BasicOpenMenu(
            this,
            player,
            closeTargets,
            openTargets
        );
        final MenuOpenEvent menuOpenEvent = new MenuOpenEvent(player, openedMenu);

        Bukkit.getServer().getPluginManager().callEvent(menuOpenEvent);

        if (menuOpenEvent.isCancelled()) {
            return;
        }

        for (Entity en : player.getWorld().getEntities()) {
            if (Metadata.hasKey(en, "tdg")) {
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
                    positionX -> Utils.setPosition(location, positionX, x1, x2, x4, x5),
                    changed
                ),
                icons
            )
        );
        openedMenu.accept(menuOpenEvent);
        TDG.getAPI().opened.put(player.getUniqueId(), openedMenu);
    }

    @NotNull
    @Override
    public String getId() {
        return id;
    }

}
