
package me.gamma.cookies.util;


import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;



public class EntityUtils {

	/**
	 * Attracts the entity towards the given location
	 * 
	 * @param entity   the entity
	 * @param location the target location
	 * @param strength the strength of the attraction
	 * @param maxr     the radius after which the attraction is zero
	 */
	public static void attract(Entity entity, Location location, double strength, double maxr) {
		Vector direction = location.clone().subtract(entity.getLocation()).toVector();
		double distance = direction.length();
		if(distance >= maxr)
			return;

		entity.setVelocity(entity.getVelocity().add(direction.multiply(strength * 0.05D * (Math.exp(4.0D / distance - 4.0D / maxr) - 1.0D))));
	}

}
