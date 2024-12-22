
package me.gamma.cookies.object.block;


import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.TileState;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.util.collection.PersistentDataObject;



public interface UpdatingGuiProvider extends BlockInventoryProvider, TileBlockRegister {

	/**
	 * Set's up the inventory after a restart of the server.
	 * 
	 * @param block     the block that holds the inventory
	 * @param inventory the inventory
	 */
	default void setupInventory(TileState block, Inventory inventory) {}


	/**
	 * Saves the inventory before a shutdown of the server.
	 * 
	 * @param block     the block that holds the inventory
	 * @param inventory the inventory
	 */
	default void saveInventory(TileState block, Inventory inventory) {}


	/**
	 * Returns the map that stores the inventory and location of registered blocks
	 * 
	 * @return the map
	 */
	Map<Location, Inventory> getInventoryMap();


	@Override
	default Inventory getGui(TileState block) {
		Location location = block.getLocation();
		Map<Location, Inventory> map = this.getInventoryMap();
		Inventory gui = map.get(location);
		if(gui == null) {
			gui = this.createGui(block);
			this.getLocations().add(location);
			map.put(location, gui);
			this.setupInventory(block, gui);
		}
		return gui;
	}


	@Override
	default boolean load(TileState block, PersistentDataObject data) {
		this.getGui(block);
		return true;
	}


	@Override
	default boolean save(TileState block, PersistentDataObject data) {
		this.saveInventory(block, this.getGui(block));
		return true;
	}


	/**
	 * Removes the inventory for the given block and returns it. Should be called when the block get's broken.
	 * 
	 * @param block the block that was broken
	 * @return the inventory of the block
	 */
	default Inventory unregisterInventory(TileState block) {
		return this.getInventoryMap().remove(block.getLocation());
	}

}
