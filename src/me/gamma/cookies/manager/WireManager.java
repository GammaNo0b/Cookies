
package me.gamma.cookies.manager;


import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.block.network.WireHolder;



public class WireManager {

	/**
	 * The amount of units the entity that holds the leash should get moved to adjust the location of the leash.
	 */
	public static final Vector Y_OFFSET_HOLDER = new Vector(0, -0.2125D, 0);

	/**
	 * The amount of units the leashed entity should get moved to adjust the location of the leash.
	 */
	public static final Vector Y_OFFSET_HELD = new Vector(0, -0.3D, -0.075);

	/**
	 * Maximum distance between two leash holders squared.
	 */
	public static final double MAX_WIRE_DISTANCE_SQUARED = 100.0D;

	/**
	 * Creates a new wire between the two given wire holders at the given block states if the distance between them is small enough.
	 * 
	 * @param first  the first wire holder
	 * @param start  the block state of the first wire holder
	 * @param second the second wire holder
	 * @param end    the location of the second wire holder
	 * @return the created wire
	 */
	public static <T> Wire createWire(WireHolder first, TileState start, WireHolder second, TileState end) {
		BlockFace face = calculateBlockFace(start.getLocation(), end.getLocation());
		BlockFace opposite = face.getOppositeFace();
		if(first.getWireLocation(start, face).distanceSquared(second.getWireLocation(end, opposite)) > MAX_WIRE_DISTANCE_SQUARED)
			return null;

		if(!first.acceptsWire(start) || !second.acceptsWire(end))
			return null;

		return new Wire(start, end, first, second, face, opposite);
	}


	/**
	 * Spawns the entity that holds one of the two ends of the leash.
	 * 
	 * @param location the location of the leash.
	 * @param offset   the offset, depending on whether the entity is holding the leash, or is leashed
	 * @return the spawned and configured entity
	 */
	static LivingEntity spawnWireHook(Location location, Vector offset) {
		Chicken entity = location.getWorld().spawn(location.clone().add(offset), Chicken.class);

		entity.setAI(false);
		entity.setAware(false);
		entity.setBaby();
		entity.setAgeLock(true);
		entity.setCollidable(false);
		entity.setGravity(false);
		entity.setInvisible(true);
		entity.setInvulnerable(true);
		entity.setSilent(true);

		return entity;
	}


	/**
	 * Calculates the block face that is nearest to the connector location.
	 * 
	 * @param location  the location of which to calculate the block face
	 * @param connector the location the wire will be connected to
	 * @return the calculated block face
	 */
	static BlockFace calculateBlockFace(Location location, Location connector) {
		Vector diff = connector.toVector().subtract(location.toVector());

		double x = diff.getX();
		double y = diff.getY();
		double z = diff.getZ();
		double ax = Math.abs(x);
		double ay = Math.abs(y);
		double az = Math.abs(z);

		if(ax >= az) {
			if(ax >= ay) {
				// x max
				return x >= 0.0D ? BlockFace.EAST : BlockFace.WEST;
			} else {
				// y max
				return x >= 0.0D ? BlockFace.UP : BlockFace.DOWN;
			}
		} else {
			if(az >= ay) {
				// z max
				return x >= 0.0D ? BlockFace.SOUTH : BlockFace.NORTH;
			} else {
				// y max
				return x >= 0.0D ? BlockFace.UP : BlockFace.DOWN;
			}
		}
	}

}
