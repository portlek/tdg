package io.github.portlek.tdg.util;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Targeted implements Scalar<Entity> {

    @NotNull
    private final Entity viewer;

    @NotNull
    private final List<Entity> entities;

    public Targeted(@NotNull Entity viewer, @NotNull List<Entity> entities) {
        this.viewer = viewer;
        this.entities = entities;
    }

    public Targeted(@NotNull Entity viewer) {
        this(viewer, viewer.getWorld().getEntities());
    }

    /**
     * gives target of player
     * @return if target is null, returns player itself or returns target entity
     */
    @NotNull
    @Override
    public Entity value() {
        Entity target = null;
        final double threshold = 1;

        for (final Entity other : entities) {
            final Vector n = other.getLocation().toVector().subtract(viewer.getLocation().toVector());

            if (viewer.getLocation().getDirection().normalize().crossProduct(n).lengthSquared() >= threshold ||
                n.normalize().dot(viewer.getLocation().getDirection().normalize()) < 0 ||
                (target != null
                    && target.getLocation().distanceSquared(viewer.getLocation()) <= other.getLocation().distanceSquared(viewer.getLocation()))) {
                continue;
            }

            target = other;
        }
        return target == null ? viewer : target;
    }

}
