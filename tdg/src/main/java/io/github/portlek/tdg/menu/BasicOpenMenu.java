package io.github.portlek.tdg.menu;

import io.github.portlek.tdg.api.LiveIcon;
import io.github.portlek.tdg.api.Menu;
import io.github.portlek.tdg.api.OpenedMenu;
import io.github.portlek.tdg.api.events.MenuCloseEvent;
import io.github.portlek.tdg.api.mock.MckLiveIcon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class BasicOpenMenu implements OpenedMenu {

    @NotNull
    private final Menu parent;

    @NotNull
    private final List<LiveIcon> liveIcons = new ArrayList<>();

    @NotNull
    private final Player player;

    public BasicOpenMenu(@NotNull Menu parent, @NotNull Player player) {
        this.parent = parent;
        this.player = player;
    }

    @NotNull
    @Override
    public LiveIcon findByEntity(@NotNull Entity entity) {
        for (LiveIcon liveIcon : liveIcons) {
            if (liveIcon.is(entity)) {
                return liveIcon;
            }
        }

        return new MckLiveIcon();
    }

    @Override
    public void close() {
        final MenuCloseEvent menuCloseEvent = new MenuCloseEvent(player, this);

        Bukkit.getServer().getPluginManager().callEvent(menuCloseEvent);

        if (menuCloseEvent.isCancelled()) {
            return;
        }

        parent.exec(menuCloseEvent);
    }

    @Override
    public void addIcons(@NotNull List<LiveIcon> liveIcons) {
        this.liveIcons.addAll(liveIcons);
    }

    @NotNull
    @Override
    public Menu getParent() {
        return parent;
    }

    @NotNull
    @Override
    public List<LiveIcon> getLiveIcons() {
        return liveIcons;
    }

}
