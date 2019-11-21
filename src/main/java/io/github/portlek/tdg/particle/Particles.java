package io.github.portlek.tdg.particle;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class Particles {

	@NotNull
	private final Location location;

	@NotNull
	private final String particle;

	private final int amount;

	private final float speed;

	public Particles(@NotNull Location location, @NotNull String particle, int amount, float speed) {
		this.location = location;
		this.particle = particle;
		this.amount = amount;
		this.speed = speed;
	}

	public void spawnParticles() {
		ParticleEffect.fromName(particle).ifPresent(effect ->
			effect.display(0.0F, 0.0F, 0.0F, speed, amount, location, 1000)
		);
	}

}