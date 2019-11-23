package io.github.portlek.tdg.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public final class Utils {

	private Utils() {
	}

	@NotNull
	public static Location setPosition(@NotNull Location loc, int positionX, double x1, double x2, double x4, double x5) {
		if (positionX == 1) {
			return getBFLoc(getLeftSide(loc, x1), -1.0);
		}

		if (positionX == 2) {
			return getBFLoc(getLeftSide(loc, x2), -0.5);
		}

		if (positionX == 3) {
			return getLeftSide(loc, 0);
		}

		if (positionX == 4) {
			return getBFLoc(getRightSide(loc, x4), -0.5);
		}

		if (positionX == 5) {
			return getBFLoc(getRightSide(loc, x5), -1.0);
		}

		return getLeftSide(loc, 0);
	}

	public static Location getRightSide(Location location, double distance) {
		float angle = location.getYaw() / 60;
		return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
	}

	public static Location getLeftSide(Location location, double distance) {
		float angle = location.getYaw() / 60;
		return location.clone().add(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
	}

	public static Location getBFLoc(Location loc, double distance) {
		Vector inverseDirectionVec = loc.getDirection().normalize().multiply(distance);
		return loc.add(inverseDirectionVec);
	}

}