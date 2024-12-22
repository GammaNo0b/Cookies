
package me.gamma.cookies.object.block;


import org.bukkit.Location;
import org.bukkit.block.TileState;

import me.gamma.cookies.util.collection.PersistentDataObject;



public interface TileBlockRegister extends BlockRegister {

	/**
	 * Get's executed on each stored tile block when the server is starting.
	 * 
	 * @param block the current block
	 * @param data  the data of the block
	 * @return returns whether the current block should be registered
	 * @see BlockRegister#load(Location, PersistentDataObject)
	 */
	default boolean load(TileState block, PersistentDataObject data) {
		return true;
	}


	/**
	 * Get's executed on each stored tile block when the server is stopping.
	 * 
	 * @param block the current block
	 * @param data  the data to be stored
	 * @return returns whether the current block should be saved
	 * @see BlockRegister#save(Location, PersistentDataObject)
	 */
	default boolean save(TileState block, PersistentDataObject data) {
		return true;
	}


	@Override
	default boolean load(Location location, PersistentDataObject data) {
		return location.getBlock().getState() instanceof TileState block && this.load(block, data);
	}


	@Override
	default boolean save(Location location, PersistentDataObject data) {
		return location.getBlock().getState() instanceof TileState block && this.save(block, data);
	}

}
