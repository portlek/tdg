package io.github.portlek.tdg.events.abs;

import io.github.portlek.tdg.LiveIcon;
import io.github.portlek.tdg.OpenedMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class IconEvent extends MenuEvent {

    @NotNull
    private final LiveIcon liveIcon;

    public IconEvent(@NotNull Player who, @NotNull OpenedMenu menu, @NotNull LiveIcon liveIcon) {
        super(who, menu);
        this.liveIcon = liveIcon;
    }

    @NotNull
    public LiveIcon getLiveIcon() {
        return liveIcon;
    }
}