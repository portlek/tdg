package io.github.portlek.tdg.menu;

import io.github.portlek.tdg.LiveIcon;
import io.github.portlek.tdg.Menu;
import io.github.portlek.tdg.OpenedMenu;
import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.events.MenuCloseEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class BasicOpenMenu implements OpenedMenu {

    @NotNull
    private final Player player;

    @NotNull
    private final Menu menu;

    @NotNull
    private final List<LiveIcon> liveIcons = new ArrayList<>();

    public BasicOpenMenu(@NotNull Player player, @NotNull Menu menu) {
        this.player = player;
        this.menu = menu;
    }

    @Override
    public void close() {
        final MenuCloseEvent menuCloseEvent = new MenuCloseEvent(player, this);

        Bukkit.getServer().getPluginManager().callEvent(menuCloseEvent);

        if (menuCloseEvent.isCancelled()) {
            return;
        }

        liveIcons.forEach(LiveIcon::close);
        menu.accept(menuCloseEvent);
        TDG.getAPI().opened.remove(player.getUniqueId());
    }

    @Override
    public void addIcons(@NotNull List<LiveIcon> liveIcons) {
        this.liveIcons.addAll(liveIcons);
    }

}
