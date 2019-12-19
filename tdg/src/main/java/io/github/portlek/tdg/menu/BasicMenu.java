package io.github.portlek.tdg.menu;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.action.OpenMenuAct;
import io.github.portlek.tdg.api.*;
import io.github.portlek.tdg.api.events.MenuCloseEvent;
import io.github.portlek.tdg.api.events.MenuOpenEvent;
import io.github.portlek.tdg.api.mock.MckOpenMenu;
import io.github.portlek.tdg.target.BasicTarget;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cactoos.list.Joined;
import org.cactoos.map.MapEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public final class BasicMenu implements Menu {

    @NotNull
    private final String id;

    @NotNull
    private final List<String> commands;

    @NotNull
    private final Map.Entry<List<Requirement>, List<Target<MenuCloseEvent>>> closeTargets;

    @NotNull
    private final Map.Entry<List<Requirement>, List<Target<MenuOpenEvent>>> openTargets;

    public BasicMenu(@NotNull String id, @NotNull List<String> commands,
                     @NotNull Map.Entry<List<Requirement>, List<Target<MenuCloseEvent>>> closeTargets,
                     @NotNull Map.Entry<List<Requirement>, List<Target<MenuOpenEvent>>> openTargets, int x1, int x2, int x4,
                     int x5, @NotNull List<Icon> icons) {
        this.id = id;
        this.commands = commands;
        this.closeTargets = new MapEntry<>(
            closeTargets.getKey(),
            new Joined<>(
                new BasicTarget<>(
                    t -> t.getOpenedMenu().getLiveIcons().forEach(LiveIcon::close)
                ),
                closeTargets.getValue()
            )
        );
        this.openTargets = new MapEntry<>(
            openTargets.getKey(),
            new Joined<>(
                new BasicTarget<>(
                    new OpenMenuAct(
                        icons,
                        x1,x2,x4,x5
                    )
                ),
                openTargets.getValue()
            )
        );
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
        final OpenedMenu oldMenu = TDG.getAPI().menus.opened.getOrDefault(player.getUniqueId(), new MckOpenMenu());

        if (!(oldMenu instanceof MckOpenMenu)) {
            oldMenu.close();
            TDG.getAPI().menus.opened.remove(player.getUniqueId());
        }

        final OpenedMenu openedMenu = new BasicOpenMenu(
            this,
            player
        );
        final MenuOpenEvent menuOpenEvent = new MenuOpenEvent(player, openedMenu, changed);

        Bukkit.getServer().getPluginManager().callEvent(menuOpenEvent);

        if (menuOpenEvent.isCancelled()) {
            return;
        }

        accept(menuOpenEvent);
    }

    @NotNull
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void accept(@NotNull MenuOpenEvent event) {
        if (openTargets.getKey().stream().allMatch(requirement -> requirement.control(event))) {
            openTargets.getValue().forEach(target -> target.handle(event));
        }
    }

    @Override
    public void exec(@NotNull MenuCloseEvent event) {
        if (closeTargets.getKey().stream().allMatch(requirement -> requirement.control(event))) {
            closeTargets.getValue().forEach(target -> target.handle(event));
        }
    }

}
