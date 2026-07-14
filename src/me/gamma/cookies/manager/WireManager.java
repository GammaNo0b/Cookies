
package me.gamma.cookies.manager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.ChunkPersistentDataStorage;
import me.gamma.cookies.object.Ticker;
import me.gamma.cookies.object.block.network.Wire;
import me.gamma.cookies.object.block.network.WireHolder;
import me.gamma.cookies.object.item.resources.WireItem;
import me.gamma.cookies.util.collection.MapUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class WireManager implements Ticker, ChunkPersistentDataStorage {

	private static final String TAG_WIRES = "wires";

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
	public static final double MAX_WIRE_DISTANCE_SQUARED = 10.0D * 10.0D;

	/**
	 * Singular instance of the wire manager.
	 */
	public static final WireManager WIRE_MANAGER = new WireManager();

	/**
	 * Map of all created wires per chunk.
	 */
	private final Map<Chunk, Set<Wire<?>>> wires = new HashMap<>();

	private WireManager() {}


	@Override
	public void load(Chunk chunk, PersistentDataObject object) {
		List<PersistentDataObject> array = object.getObjectList(TAG_WIRES);
		if(array == null || array.isEmpty())
			return;

		Set<Wire<?>> set = new HashSet<>();
		for(PersistentDataObject o : array) {
			Wire<?> wire = Wire.load(o);
			if(wire != null)
				set.add(wire);
		}
		if(set.isEmpty()) {
			this.wires.remove(chunk);
		} else {
			this.wires.put(chunk, set);
		}
	}


	@Override
	public void save(Chunk chunk, PersistentDataObject object) {
		Set<Wire<?>> set = this.wires.get(chunk);
		if(set == null || set.isEmpty()) {
			object.setObjectList(TAG_WIRES, new ArrayList<>());
			return;
		}

		List<PersistentDataObject> array = new ArrayList<>(set.size());

		for(Wire<?> wire : set) {
			PersistentDataObject o = new PersistentDataObject(object.getAdapterContext());
			wire.save(o);
			array.add(o);
		}

		object.setObjectList(TAG_WIRES, array);
	}


	@Override
	public String getIdentifier() {
		return "wire_manager";
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public void tick() {
		for(World world : Bukkit.getWorlds()) {
			for(Chunk chunk : world.getLoadedChunks()) {
				Set<Wire<?>> wires = this.wires.get(chunk);
				if(wires != null)
					wires.forEach(Wire::transfer);
			}
		}
	}


	/**
	 * Creates a new wire between the two given wire holders at the given block states if the distance between them is small enough.
	 * 
	 * @param first    the first wire holder
	 * @param start    the block state of the first wire holder
	 * @param second   the second wire holder
	 * @param end      the location of the second wire holder
	 * @param wireItem the item used to create the wire
	 * @return the created wire
	 */
	public <T> Wire<T> createWire(WireHolder<T> first, Block start, WireHolder<T> second, Block end, WireItem wireItem) {
		if(!first.acceptsWire() || !second.acceptsWire())
			return null;

		if(first.hasWireTo(end) || second.hasWireTo(start))
			return null;

		BlockFace face = calculateBlockFace(start.getLocation(), end.getLocation());
		BlockFace opposite = face.getOppositeFace();
		if(first.getWireLocation(start.getLocation(), face).distanceSquared(second.getWireLocation(end.getLocation(), opposite)) > MAX_WIRE_DISTANCE_SQUARED)
			return null;

		LivingEntity holder = spawnWireHook(first.getWireLocation(start.getLocation(), face), WireManager.Y_OFFSET_HOLDER);
		LivingEntity held = spawnWireHook(second.getWireLocation(end.getLocation(), face.getOppositeFace()), WireManager.Y_OFFSET_HELD);
		held.setLeashHolder(holder);

		Wire<T> wire = new Wire<>(start.getLocation(), end.getLocation(), first, second, holder.getUniqueId(), held.getUniqueId(), wireItem);

		Chunk chunk1 = start.getChunk();
		Chunk chunk2 = end.getChunk();
		MapUtils.getOrStoreDefault(this.wires, chunk1, (Supplier<Set<Wire<?>>>) HashSet::new).add(wire);
		if(!chunk1.equals(chunk2))
			MapUtils.getOrStoreDefault(this.wires, chunk2, (Supplier<Set<Wire<?>>>) HashSet::new).add(wire);

		return wire;
	}


	/**
	 * Removes the given wire.
	 * 
	 * @param wire the wire
	 */
	public void removeWire(Wire<?> wire) {
		Chunk chunk1 = wire.getFirstChunk();
		Chunk chunk2 = wire.getSecondChunk();
		Set<Chunk> chunks = chunk1.equals(chunk2) ? Set.of(chunk1) : Set.of(wire.getFirstChunk(), wire.getSecondChunk());
		for(Chunk chunk : chunks) {
			Set<Wire<?>> set = this.wires.get(chunk);
			if(set != null)
				set.remove(wire);
		}
	}


	/**
	 * Spawns the entity that holds one of the two ends of the leash.
	 * 
	 * @param location the location of the leash.
	 * @param offset   the offset, depending on whether the entity is holding the leash, or is leashed
	 * @return the spawned and configured entity
	 */
	public static LivingEntity spawnWireHook(Location location, Vector offset) {
		final double scale = 1.0D;

		Chicken entity = location.getWorld().spawn(location.clone().add(offset.clone().multiply(scale)), Chicken.class);

		entity.setAI(false);
		entity.setAware(false);
		entity.setBaby();
		entity.setAgeLock(true);
		entity.setCollidable(false);
		entity.setGravity(false);
		entity.setInvisible(true);
		entity.setInvulnerable(true);
		entity.setSilent(true);

		entity.getAttribute(Attribute.SCALE).setBaseValue(scale);

		return entity;
	}


	/**
	 * Calculates the block face that is nearest to the connector location.
	 * 
	 * @param location  the location of which to calculate the block face
	 * @param connector the location the wire will be connected to
	 * @return the calculated block face
	 */
	public static BlockFace calculateBlockFace(Location location, Location connector) {
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
				return y >= 0.0D ? BlockFace.UP : BlockFace.DOWN;
			}
		} else {
			if(az >= ay) {
				// z max
				return z >= 0.0D ? BlockFace.SOUTH : BlockFace.NORTH;
			} else {
				// y max
				return y >= 0.0D ? BlockFace.UP : BlockFace.DOWN;
			}
		}
	}

}
