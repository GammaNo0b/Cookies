
package me.gamma.cookies.object.block.network;


import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.item.resources.WireItem;
import me.gamma.cookies.object.tile.TileEntityStorage;



public interface WireHolder<T> {

	/**
	 * Returns the provider of the resource of this wire holder.
	 * 
	 * @return the resource provider
	 */
	Provider<T> getWireProvider();

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
	 * @param location the location of the other possible wire holder
	 * @param wireItem the item used to create the item
	 * @return if the wire was created
	 */
	default boolean createWire(Location location, WireItem wireItem) {
		Block other = location.getBlock();
		WireHolder<T> holder = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(other);
		if(holder == null)
			return false;

		return this.createWire(holder, other, wireItem);
	}


	/**
	 * Creates a wire connecting this wire holder and the given wire holder at the given locations.
	 * 
	 * @param holder   the other wire holder
	 * @param other    the block state of the other wire holder
	 * @param wireItem the item used to create the item
	 * @return if the wire was created
	 */
	boolean createWire(WireHolder<T> holder, Block other, WireItem wireItem);


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
	 * Returns the offset of the center of the block.
	 * 
	 * @param face the side at which the wire should be attached
	 * @return the offset in the given direction
	 */
	default Vector getWireOffset(BlockFace face) {
		return this.getCenter().add(face.getDirection().multiply(this.getHalfWidth()));
	}


	/**
	 * Calculates the location at which the entity holding the wire should be placed.
	 * 
	 * @param pos  the block position
	 * @param face the side at which the wire should be attached
	 * @return the location for the wire
	 */
	default Location getWireLocation(Location pos, BlockFace face) {
		return pos.add(this.getWireOffset(face));
	}


	/**
	 * Checks if the given block can accept another wire.
	 * 
	 * @return if the block can accept another wire
	 */
	boolean acceptsWire();

	/**
	 * Adds the given wire to the given block.
	 * 
	 * @param wire the wire
	 */
	void addWire(Wire<T> wire);

	/**
	 * Removes the given wire from the given block.
	 * 
	 * @param wire the wire
	 */
	void removeWire(Wire<T> wire);

	/**
	 * Removes the lastly added wire to this block. If this block has no wires, the method returns null.
	 * 
	 * @return the removed wire or null
	 */
	Wire<T> removeWire();

	/**
	 * Removes all connected wires.
	 */
	void removeWires();

	/**
	 * Returns if this wire holder already has a wire to the given block.
	 * 
	 * @param destination the target block
	 * @return if such a wire exists
	 */
	boolean hasWireTo(Block destination);

}
