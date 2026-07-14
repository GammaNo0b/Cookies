
package me.gamma.cookies.object.block;


import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.object.gui.InventoryProvider;
import me.gamma.cookies.object.tile.ContainerTile;



public interface BlockInventoryProvider extends InventoryProvider<Block> {

	@Override
	default SoundCategory getSoundCategory() {
		return SoundCategory.BLOCKS;
	}


	/**
	 * Loads this inventory provider.
	 * 
	 * @param block the block
	 * @return if successful
	 */
	default boolean loadInventory(Block block) {
		return true;
	}


	/**
	 * Saves this inventory provider.
	 * 
	 * @param block the block
	 * @return if successful
	 */
	default boolean saveInventory(Block block) {
		return true;
	}


	/**
	 * Returns the container at the given block of this block inventory provider.
	 * 
	 * @param block the block
	 * @return the container
	 */
	ContainerTile getContainer(Block block);


	@Override
	default boolean onMainInventoryInteract(Player player, Block data, Inventory gui, InventoryClickEvent event) {
		ContainerTile container = this.getContainer(data);
		return container == null || container.onMainInventoryInteract(player, gui, event);
	}


	@Override
	default boolean onPlayerInventoryInteract(Player player, Block data, PlayerInventory gui, InventoryClickEvent event) {
		ContainerTile container = this.getContainer(data);
		return container == null || container.onPlayerInventoryInteract(player, gui, event);
	}


	@Override
	default boolean onInventoryClose(Player player, Block data, Inventory gui, InventoryCloseEvent event) {
		ContainerTile container = this.getContainer(data);
		return container == null || container.onInventoryClose(player, gui, event);
	}

}
