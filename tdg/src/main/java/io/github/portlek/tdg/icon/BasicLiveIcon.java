package io.github.portlek.tdg.icon;

import io.github.portlek.tdg.api.LiveIcon;
import io.github.portlek.tdg.api.Target;
import io.github.portlek.tdg.api.events.IconClickEvent;
import io.github.portlek.tdg.api.events.IconHoverEvent;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class BasicLiveIcon implements LiveIcon {

    @NotNull
    private final List<ArmorStand> armorStands;

    @NotNull
    private final Player viewer;

    @NotNull
    private final List<Target<IconClickEvent>> clickTargets;

    @NotNull
    private final List<Target<IconHoverEvent>> hoverTargets;

    public BasicLiveIcon(@NotNull List<ArmorStand> armorStands, @NotNull Player viewer,
                         @NotNull List<Target<IconClickEvent>> clickTargets,
                         @NotNull List<Target<IconHoverEvent>> hoverTargets) {
        this.armorStands = armorStands;
        this.viewer = viewer;
        this.clickTargets = clickTargets;
        this.hoverTargets = hoverTargets;
    }

    @Override
    public boolean is(@NotNull Entity entity) {
        if (entity instanceof ArmorStand) {
            return armorStands.contains(entity);
        }

        return false;
    }

    @Override
    public void close() {
        // TODO: 24/11/2019
    }

    @Override
    public void accept(@NotNull IconClickEvent event) {
        clickTargets.forEach(target -> target.handle(event));
    }

    @Override
    public void accept(@NotNull IconHoverEvent event) {
        hoverTargets.forEach(target -> target.handle(event));
    }

}
