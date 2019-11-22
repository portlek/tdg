package io.github.portlek.tdg.icon;

import io.github.portlek.tdg.Icon;
import io.github.portlek.tdg.LiveIcon;
import io.github.portlek.tdg.events.IconClickEvent;
import io.github.portlek.tdg.events.IconHoverEvent;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class BasicLiveIcon implements LiveIcon {

    @NotNull
    private final Icon icon;

    @NotNull
    private final List<ArmorStand> armorStands;

    @NotNull
    private final Player viewer;

    public BasicLiveIcon(@NotNull Icon icon, @NotNull List<ArmorStand> armorStands, @NotNull Player viewer) {
        this.icon = icon;
        this.armorStands = armorStands;
        this.viewer = viewer;
    }

    @Override
    public boolean is(@NotNull Entity entity) {
        return false;
    }

    @Override
    public void playSound() {

    }

    @Override
    public void accept(@NotNull IconClickEvent event) {

    }

    @Override
    public void accept(@NotNull IconHoverEvent event) {

    }

    @Override
    public void close() {

    }
}
