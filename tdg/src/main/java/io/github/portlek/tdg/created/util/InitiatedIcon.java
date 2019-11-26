package io.github.portlek.tdg.created.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

public final class InitiatedIcon implements Scalar<Location> {

    @NotNull
    private final Player player;

    @NotNull
    private final Location location;

    private final int positionY;

    public InitiatedIcon(@NotNull Player player, @NotNull Location location, int positionY) {
        this.player = player;
        this.location = location;
        this.positionY = positionY;
    }

    @Override
    public Location value() {
        final Vector playerDirection = player.getLocation().getDirection();
        final Vector direction = playerDirection.normalize();

        direction.multiply(-2);
        location.setDirection(direction);

        final float yaw = (float) Math.toDegrees(Math.atan2(player.getLocation().getZ() - location.getZ(), player.getLocation().getX() - location.getX())) - 90;
        final float pitch = (float) Math.toDegrees(Math.atan2(player.getLocation().getZ() - location.getZ(), player.getLocation().getX() - location.getX())) - 90;

        location.setYaw(yaw);
        location.setPitch(pitch);

        if (positionY == 2) {
            location.add(0.0, 1.7, 0.0);
        }

        return location;
    }

}
