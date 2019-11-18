package io.github.portlek.tdg.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

public final class Targeted implements Scalar<Entity> {

    @NotNull
    private final Player player;

    public Targeted(@NotNull Player player) {
        this.player = player;
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

        for (Entity entity : player.getLocation().getChunk().getEntities()) {
            final Vector n = entity.getLocation().toVector().subtract(entity.getLocation().toVector());

            if (entity.getLocation().getDirection().normalize().crossProduct(n).lengthSquared() >= threshold ||
                n.normalize().dot(entity.getLocation().getDirection().normalize()) < 0 ||
                (target != null &&
                target.getLocation().distanceSquared(entity.getLocation()) <= entity.getLocation().distanceSquared(entity.getLocation()))) {
                continue;
            }

            target = entity;
        }

        return target == null ? player : target;
    }

}
