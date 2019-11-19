package io.github.portlek.tdg.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Utils {

	@Nullable
	public static Location setPosition(@NotNull Location loc, int positionX, int positionY, double x1, double x2, double x4, double x5) {
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

		return null;
	}
    
    /**
    * Returns a location with a specified distance away from the right side of
    * a location.
    *
    * @param location The origin location
    * @param distance The distance to the right
    * @return the location of the distance to the right
    */
    public static Location getRightSide(Location location, double distance) {
        final float angle = location.getYaw() / 60;

        return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }

    /**
    * Gets a location with a specified distance away from the left side of a
    * location.
    *
    * @param location The origin location
    * @param distance The distance to the left
    * @return the location of the distance to the left
    */
    public static Location getLeftSide(Location location, double distance) {
        final float angle = location.getYaw() / 60;

        return location.clone().add(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }
    
    /**
    * Gets a location with a specified distance away from the behind or front
    * from a location.
    */
    public static Location getBFLoc(Location loc, double distance) {
        return loc.add(loc.getDirection().normalize().multiply(distance));
    }

}