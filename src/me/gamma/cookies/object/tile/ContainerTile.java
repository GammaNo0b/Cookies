
package me.gamma.cookies.object.tile;


import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.block.BlockInventoryProvider;
import me.gamma.cookies.object.gui.InventoryHandler;



public interface ContainerTile extends InventoryHandler, BlockInventoryHolder {

	/**
	 * Returns the block inventory provider.
	 * 
	 * @return the provider
	 */
	BlockInventoryProvider getInventoryProvider();


	@Override
	default Inventory getInventory() {
		return this.getInventoryProvider().getGui(this.getBlock());
	}


	/**
	 * Loads the inventory of this container.
	 * 
	 * @return if successful
	 */
	default boolean loadInventory() {
		return this.getInventoryProvider().loadInventory(this.getBlock());
	}


	/**
	 * Saves the inventory of this container.
	 * 
	 * @return if successful
	 */
	default boolean saveInventory() {
		return this.getInventoryProvider().saveInventory(this.getBlock());
	}


	default void setupInventory(Inventory inventory) {}


	default void finishInventory(Inventory inventory) {}

}
