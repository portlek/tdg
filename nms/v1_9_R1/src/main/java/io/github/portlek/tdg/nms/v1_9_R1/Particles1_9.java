package io.github.portlek.tdg.nms.v1_9_R1;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public final class Particles1_9 {

    @NotNull
    private final Location location;

    @NotNull
    private final Particle particle;

    private final int amount;

    private final int speed;

    public Particles1_9(@NotNull Location location, @NotNull Particle particle, int amount, int speed) {
        this.location = location;
        this.particle = particle;
        this.amount = amount;
        this.speed = speed;
    }

    public void spawn() {
        if (location.getWorld() == null) {
            throw new IllegalStateException("World of the location cannot be null!");
        }

        location.getWorld().spawnParticle(particle, location, amount, 0.0, 0.0, 0.0, speed);
    }

}