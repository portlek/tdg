package io.github.portlek.tdg.api.events.abs;

import io.github.portlek.tdg.api.LiveIcon;
import io.github.portlek.tdg.api.OpenedMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class IconEvent extends MenuEvent {

    @NotNull
    private final LiveIcon liveIcon;

    public IconEvent(@NotNull Player who, @NotNull OpenedMenu openedMenu, @NotNull LiveIcon liveIcon) {
        super(who, openedMenu);
        this.liveIcon = liveIcon;
    }

    @NotNull
    public LiveIcon getLiveIcon() {
        return liveIcon;
    }
}