
package me.gamma.cookies.object.block;


import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.util.WorldUtils;



public interface CustomBlockHandler {

	/**
	 * Places an instance of this custom block at the given block position.
	 * 
	 * @param block the block position
	 * @return if the block was placed
	 */
	default boolean place(Block block) {
		return this.place(null, null, block);
	}


	/**
	 * Places an instance of this custom block at the given block position. The player can be null, if the block is set using a command from the console
	 * for example. In this case {@link AbstractCustomBlock#canPlace(Player, Block)} has to return false if the player has to be non null.
	 * 
	 * @param player the player that placed the block or null
	 * @param stack  the item stack from which the block got placed
	 * @param block  the block position
	 * @return if the block was placed
	 */
	boolean place(Player player, ItemStack stack, Block block);


	/**
	 * Checks whether the player can place the block.
	 * 
	 * @param player the player
	 * @param block  the block
	 * @return whether the block can be placed by the player
	 */
	default boolean canPlace(Player player, Block block) {
		return player == null || WorldUtils.canPlace(player, block);
	}


	/**
	 * Get's executed when the player places a from this class handled block.
	 * 
	 * @param player   the player
	 * @param holder   the holder of data to be transferred on the block
	 * @param block    the placed block
	 * @param usedItem the used item stack
	 * @return if the block place event should be cancelled or not
	 */
	boolean onBlockPlace(Player player, PersistentDataHolder holder, TileState block);

	/**
	 * Get's executed when the player breaks a from this class handled block.
	 * 
	 * @param player the player
	 * @param block  the block that got broken
	 * @param event  the fired event
	 * @return if the block break event should be cancelled or not
	 */
	boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event);


	/**
	 * Get's executed when a block causes a from this class handled block to explode.
	 * 
	 * @param block the block going to explode
	 * @param event the fired event
	 * @return if the block should not be exploded
	 */
	default boolean onBlockExplodesBlock(TileState block, BlockExplodeEvent event) {
		return true;
	}


	/**
	 * Get's executed when an entity causes a from this class handled block to explode.
	 * 
	 * @param block the block going to explode
	 * @param event the fired event
	 * @return if the block should not be exploded
	 */
	default boolean onEntityExplodesBlock(TileState block, EntityExplodeEvent event) {
		return true;
	}


	/**
	 * Get's executed when the player clicks with the right mouse button on a from this class handled block with an item.
	 * 
	 * @param player the player
	 * @param block  the clicked block
	 * @param stack  the used item stack
	 * @param event  the fired event
	 * @return whether the event should be cancelled or not
	 */
	default boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		return false;
	}


	/**
	 * Get's executed when the player clicks with the left mouse button on a from this class handled block with an item.
	 * 
	 * @param player the player
	 * @param block  the clicked block
	 * @param stack  the used item stack
	 * @param event  the fired event
	 * @return whether the event should be cancelled or not
	 */
	default boolean onBlockLeftClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		return false;
	}


	/**
	 * Get's executed when the player interacts physically (like jumping) with a from this class handled block.
	 * 
	 * @param player the player
	 * @param block  the block
	 * @param event  the fired event
	 * @return whether the event should be cancelled or not
	 */
	default boolean onBlockInteract(Player player, TileState block, PlayerInteractEvent event) {
		return false;
	}


	/**
	 * get's executed when the redstone current at the given block changes.
	 * 
	 * @param block      the block
	 * @param oldCurrent the old redstone current
	 * @param newCurrent the new redstone current
	 * @param event      the fired event
	 */
	@Deprecated
	default void onRedstoneChange(TileState block, int oldCurrent, int newCurrent, BlockRedstoneEvent event) {}

}
