package io.github.portlek.tdg.icon;

import io.github.portlek.tdg.LiveIcon;
import io.github.portlek.tdg.Target;
import io.github.portlek.tdg.events.IconClickEvent;
import io.github.portlek.tdg.events.IconHoverEvent;
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
        return armorStands.contains(entity);
    }

    @Override
    public void playSound() {

    }

    @Override
    public void close() {

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
