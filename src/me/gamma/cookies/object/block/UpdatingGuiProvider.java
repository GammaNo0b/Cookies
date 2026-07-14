
package me.gamma.cookies.object.block;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.util.collection.MapUtils;



public interface UpdatingGuiProvider extends BlockInventoryProvider {

	Map<Class<?>, Map<Location, Inventory>> inventories = new HashMap<>();

	/**
	 * Set's up the inventory after a restart of the server.
	 *
	 * @param block     the block that holds the inventory
	 * @param inventory the inventory
	 */
	default void setupInventory(Block block, Inventory inventory) {
		this.getContainer(block).setupInventory(inventory);
	}


	/**
	 * Saves the inventory before a shutdown of the server.
	 * 
	 * @param block     the block that holds the inventory
	 * @param inventory the inventory
	 */
	default void saveInventory(Block block, Inventory inventory) {
		this.getContainer(block).finishInventory(inventory);
	}


	@Override
	default Inventory getGui(Block block) {
		Location location = block.getLocation();
		Map<Location, Inventory> map = MapUtils.getOrStoreDefault(inventories, this.getClass(), (Supplier<Map<Location, Inventory>>) HashMap::new);
		Inventory gui = map.get(location);
		if(gui == null) {
			gui = this.createGui(block);
			map.put(location, gui);
			this.setupInventory(block, gui);
		}
		return gui;
	}


	@Override
	default boolean loadInventory(Block block) {
		this.getGui(block);
		return true;
	}


	@Override
	default boolean saveInventory(Block block) {
		this.saveInventory(block, this.getGui(block));
		return true;
	}


	/**
	 * Removes the inventory of this block and returns it. Should be called when the block get's broken.
	 * 
	 * @param block the block
	 * @return the inventory of the block
	 */
	default Inventory unregisterInventory(Block block) {
		Map<Location, Inventory> map = inventories.get(this.getClass());
		if(map == null)
			return null;

		return map.remove(block.getLocation());
	}

}
