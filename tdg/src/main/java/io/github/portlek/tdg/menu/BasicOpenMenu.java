package io.github.portlek.tdg.menu;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.api.LiveIcon;
import io.github.portlek.tdg.api.Menu;
import io.github.portlek.tdg.api.OpenedMenu;
import io.github.portlek.tdg.api.Target;
import io.github.portlek.tdg.api.events.MenuCloseEvent;
import io.github.portlek.tdg.api.events.MenuOpenEvent;
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

    @NotNull
    private final List<Target<MenuCloseEvent>> closeTargets;

    @NotNull
    private final List<Target<MenuOpenEvent>> openTargets;

    public BasicOpenMenu(@NotNull Menu parent, @NotNull Player player,
                         @NotNull List<Target<MenuCloseEvent>> closeTargets,
                         @NotNull List<Target<MenuOpenEvent>> openTargets) {
        this.parent = parent;
        this.player = player;
        this.closeTargets = closeTargets;
        this.openTargets = openTargets;
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

        liveIcons.forEach(LiveIcon::close);
        accept(menuCloseEvent);
        TDG.getAPI().opened.remove(player.getUniqueId());
    }

    @Override
    public void addIcons(@NotNull List<LiveIcon> liveIcons) {
        this.liveIcons.addAll(liveIcons);
    }

    @Override
    public void accept(@NotNull MenuOpenEvent event) {
        openTargets.forEach(target -> target.handle(event));
    }

    @Override
    public void accept(@NotNull MenuCloseEvent event) {
        closeTargets.forEach(target -> target.handle(event));
    }

    @NotNull
    @Override
    public Menu getParent() {
        return parent;
    }

}
