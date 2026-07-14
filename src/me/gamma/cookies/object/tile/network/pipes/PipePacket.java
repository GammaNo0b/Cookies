
package me.gamma.cookies.object.tile.network.pipes;


import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import me.gamma.cookies.object.DataStorage;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class PipePacket implements DataStorage {

	private static final String KEY_TICKS_LEFT = "ticksleft";
	private static final String KEY_PHASE = "phase";
	private static final String KEY_FACE = "face";

	/**
	 * Number of ticks left until the target is reached.
	 */
	protected double ticksLeft = 0.0D;

	/**
	 * The {@link Phase} of the packet, either entering or exiting.
	 */
	protected Phase phase = Phase.ENTER;

	/**
	 * The face this packet either came from or moves towards.
	 */
	protected BlockFace face = BlockFace.SELF;

	/**
	 * Sets the location of this packet.
	 * 
	 * @param position the position
	 */
	protected void setPosition(Location position) {}


	/**
	 * Sets the target location of this packet.
	 * 
	 * @param target the target position
	 */
	protected void setTarget(Location target) {}


	/**
	 * Updates this packet.
	 */
	protected void update() {}


	/**
	 * Removes this packet.
	 */
	protected void remove() {}


	/**
	 * Drops this packet at the given location.
	 * 
	 * @param pos  the position
	 * @param face the block face
	 */
	protected void drop() {}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		this.ticksLeft = data.getDouble(KEY_TICKS_LEFT, 1.0D);

		this.phase = PersistentDataUtils.getEnum(data, KEY_PHASE, Phase.class);
		if(this.phase == null)
			return false;

		this.face = PersistentDataUtils.getEnum(data, KEY_FACE, BlockFace.class);
		if(this.face == null)
			return false;

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		data.setDouble(KEY_TICKS_LEFT, this.ticksLeft);
		PersistentDataUtils.setEnum(data, KEY_PHASE, this.phase);
		PersistentDataUtils.setEnum(data, KEY_FACE, this.face);

		return true;
	}

	public static enum Phase {

		ENTER,
		EXIT;

	}

}
