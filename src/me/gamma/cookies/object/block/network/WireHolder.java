
package me.gamma.cookies.object.block.network;


import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.util.Vector;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.manager.WireManager;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.item.resources.WireItem;



public interface WireHolder<T> {

	/**
	 * Returns the provider of this wire holder.
	 * 
	 * @param block the block
	 * @return the resource provider
	 */
	Provider<T> getWireProvider(TileState block);

	/**
	 * Returns the type of component this wireholder has.
	 * 
	 * @return the component type
	 */
	WireComponentType getWireComponentType();


	/**
	 * Creates a wire connecting this wire holder with a wire holder sitting at the given location. If the location does not have a wire holder, no wire
	 * will be created and this method will return false.
	 * 
	 * @param state    the block of this wire holder
	 * @param location the location of the other possible wire holder
	 * @param wireItem the item used to create the item
	 * @return if the wire was created
	 */
	@SuppressWarnings("unchecked")
	default boolean createWire(TileState state, Location location, WireItem wireItem) {
		if(!(location.getBlock().getState() instanceof TileState other))
			return false;
		if(!(Blocks.getCustomBlockFromBlock(other) instanceof WireHolder<?> holder))
			return false;
		try {
			return this.createWire(state, (WireHolder<T>) holder, other, wireItem);
		} catch(ClassCastException _) {
			return false;
		}
	}


	/**
	 * Creates a wire connecting this wire holder and the given wire holder at the given locations.
	 * 
	 * @param state    the block state of this wire holder
	 * @param holder   the other wire holder
	 * @param other    the block state of the other wire holder
	 * @param wireItem the item used to create the item
	 * @return if the wire was created
	 */
	default boolean createWire(TileState state, WireHolder<T> holder, TileState other, WireItem wireItem) {
		return WireManager.createWire(this, state, holder, other, wireItem) != null;
	}


	/**
	 * Returns the half width of the block.
	 * 
	 * Defaults to the half width of a player head beeing <code>0.25</code>.
	 * 
	 * @return the half witdh
	 */
	default double getHalfWidth() {
		return 0.25D;
	}


	/**
	 * Returns the center location of the block relative to the blocks coordinates.
	 * 
	 * Defaults to the center of a player head beeing <code>(0.5 | 0.25 | 0.5)</code>.
	 * 
	 * @return the center of the block
	 */
	default Vector getCenter() {
		return new Vector(0.5D, 0.25D, 0.5D);
	}


	/**
	 * Returns the offset the wire is placed from the block location.
	 * 
	 * In more detail, the location of the entity holding the wire will be calculated using
	 * 
	 * <pre>
	 * {@code
	 * WireHolder holder = ...
	 * TileState block = ...
	 * BlockFace face = ...
	 * Location wireLocation = block.getLocation().add(holder.getWireOffset(face));
	 * }
	 * </pre>
	 * 
	 * The default implmenetation calculates the offset by scaling the direction of the given {@link BlockFace} by {@link WireHolder#getHalfWidth()} and
	 * adding this to {@link WireHolder#getCenter()}.
	 * 
	 * @param face the side at which the wire should be attached
	 * @return the wire offset in form of a vector
	 */
	default Vector getWireOffset(BlockFace face) {
		return this.getCenter().add(face.getDirection().multiply(this.getHalfWidth()));
	}


	/**
	 * Calculates the location at which the entity holding the wire should be placed.
	 * 
	 * @param block the block
	 * @param face  the side at which the wire should be attached
	 * @return the location for the wire
	 */
	default Location getWireLocation(TileState block, BlockFace face) {
		return block.getLocation().add(this.getWireOffset(face));
	}


	/**
	 * Checks if the given block can accept another wire.
	 * 
	 * @param block the block
	 * @return if the block can accept another wire
	 */
	boolean acceptsWire(TileState block);

	/**
	 * Adds the given wire to the given block.
	 * 
	 * @param block the block
	 * @param wire  the wire
	 */
	void addWire(TileState block, Wire<T> wire);

	/**
	 * Removes the given wire from the given block.
	 * 
	 * @param block the block
	 * @param wire  the wire
	 */
	void removeWire(TileState block, Wire<T> wire);

	/**
	 * Removes the lastly added wire to this block. If this block has no wires, the method returns null.
	 * 
	 * @param block the block
	 * @return the removed wire or null
	 */
	Wire<T> removeWire(TileState block);

}
