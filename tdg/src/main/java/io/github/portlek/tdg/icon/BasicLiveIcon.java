package io.github.portlek.tdg.icon;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.api.Icon;
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
    private final Icon parent;

    @NotNull
    private final List<ArmorStand> armorStands;

    @NotNull
    private final List<Target<IconClickEvent>> clickTargets;

    @NotNull
    private final List<Target<IconHoverEvent>> hoverTargets;

    public BasicLiveIcon(@NotNull Icon parent, @NotNull List<ArmorStand> armorStands,
                         @NotNull List<Target<IconClickEvent>> clickTargets,
                         @NotNull List<Target<IconHoverEvent>> hoverTargets) {
        this.parent = parent;
        this.armorStands = armorStands;
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
        for (ArmorStand armorStand : armorStands) {
            TDG.getAPI().entities.remove(armorStand);
            armorStand.remove();
        }
    }

    @Override
    public void accept(@NotNull IconClickEvent event) {
        clickTargets.forEach(target -> target.handle(event));
    }

    @Override
    public void accept(@NotNull IconHoverEvent event) {
        hoverTargets.forEach(target -> target.handle(event));
    }

    @NotNull
    @Override
    public Icon getParent() {
        return parent;
    }

}
