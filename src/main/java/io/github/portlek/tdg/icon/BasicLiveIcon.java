package io.github.portlek.tdg.icon;

import io.github.portlek.tdg.Icon;
import io.github.portlek.tdg.LiveIcon;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class BasicLiveIcon implements LiveIcon {

    @NotNull
    private final Icon icon;

    @NotNull
    private final ArmorStand armorStand;

    @NotNull
    private final Player viewer;

    public BasicLiveIcon(@NotNull Icon icon, @NotNull ArmorStand armorStand, @NotNull Player viewer) {
        this.icon = icon;
        this.armorStand = armorStand;
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
    public void acceptClick() {

    }

    @Override
    public void acceptHover() {

    }

    @Override
    public void closeFor() {

    }
}
