
package me.gamma.cookies.object.block;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.util.WorldUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public interface CustomBlockHandler {

	/**
	 * 2 Tries to place this block at the given block location. Returns whether the placement was successful.
	 * 
	 * @param block the block location
	 * @param data  data to be transferred onto the block
	 * @return if the block was placed
	 */
	default boolean place(Block block, PersistentDataObject data) {
		Material oldType = block.getType();
		BlockData bdata = block.getBlockData();

		if(this.placeBlock(block)) {
			if(this.onBlockPlace(block, data)) {
				this.blockPlaced(block);
				return true;
			}
		}

		block.setType(oldType);
		block.setBlockData(bdata);
		return false;
	}


	/**
	 * 2 Tries to place this block at the given block location. Returns whether the placement was successful.
	 * 
	 * @param block the block location
	 * @param type  the material of the block to place
	 * @param data  data to be transferred onto the block
	 * @return if the block was placed
	 */
	default boolean place(Block block, Material type, PersistentDataObject data) {
		Material oldType = block.getType();
		BlockData bdata = block.getBlockData();

		if(this.placeBlock(block, type)) {
			if(this.onBlockPlace(block, data)) {
				this.blockPlaced(block);
				return true;
			}
		}

		block.setType(oldType);
		block.setBlockData(bdata);
		return false;
	}


	/**
	 * Tries to place this block by the given player at the given block location. Returns whether the placement was successful.
	 * 
	 * @param block  the block location
	 * @param player the player playing the block
	 * @param event  the fired event
	 * @return if the block was placed
	 */
	default boolean place(Block block, Player player, BlockPlaceEvent event) {
		if(!this.canPlace(player, block))
			return false;

		if(!this.placeBlock(block))
			return false;

		if(!this.onBlockPlace(block, null))
			return false;

		if(!this.onBlockPlace(block, AbstractCustomItem.getCustomData(event.getItemInHand()).getData(), player, event))
			return false;

		this.blockPlaced(block);

		return true;
	}


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
	 * Places an instance of this custom block at the given block position.
	 * 
	 * @param block the block position
	 * @param type  the material of the block to place
	 * @return if the block was placed
	 */
	boolean placeBlock(Block block, Material type);

	/**
	 * Places an instance of this custom block at the given block position.
	 * 
	 * @param block the block position
	 * @return if the block was placed
	 */
	boolean placeBlock(Block block);


	/**
	 * Is called after an instance of this custom block has been placed.
	 * 
	 * @param block the block placed
	 */
	default void blockPlaced(Block block) {}


	/**
	 * Breaks the custom block at the given block position.
	 * 
	 * @param block the block location
	 * @return if the block was broken
	 */
	default boolean breakBlock(Block block) {
		block.setType(Material.AIR);
		this.blockBroken(block);
		return true;
	}


	/**
	 * Is called after an instanceo of this custom block has been broken.
	 * 
	 * @param block the block broken
	 */
	default void blockBroken(Block block) {}


	/**
	 * Get's executed when a from this class handled block is placed.
	 * 
	 * @param block the placed block
	 * @param data  the data to be transferred onto the block
	 * @return if the block place was successful
	 */
	default boolean onBlockPlace(Block block, PersistentDataObject data) {
		return true;
	}


	/**
	 * Get's executed when the player places a from this class handled block.
	 * 
	 * @param block  the placed block
	 * @param data   the data to be transferred onto the block
	 * @param player the player
	 * @param event  the fired event
	 * 
	 * @return if the block place was successful and the event should not be cancelled
	 */
	default boolean onBlockPlace(Block block, PersistentDataObject data, Player player, BlockPlaceEvent event) {
		return true;
	}


	/**
	 * Get's executed when the player breaks a from this class handled block.
	 * 
	 * @param player the player
	 * @param block  the block that got broken
	 * @param event  the fired event
	 * @return if the block break was successful and the event should not be cancelled
	 */
	default boolean onBlockBreak(Player player, Block block, BlockBreakEvent event) {
		return true;
	}


	/**
	 * Get's executed when a block causes a from this class handled block to explode.
	 * 
	 * @param block the block going to explode
	 * @param event the fired event
	 * @return if the block should be exploded
	 */
	default boolean onBlockExplodesBlock(Block block, BlockExplodeEvent event) {
		return true;
	}


	/**
	 * Get's executed when an entity causes a from this class handled block to explode.
	 * 
	 * @param block the block going to explode
	 * @param event the fired event
	 * @return if the block should be exploded
	 */
	default boolean onEntityExplodesBlock(Block block, EntityExplodeEvent event) {
		return true;
	}


	/**
	 * Get's executed when the player clicks with the right mouse button on a from this class handled block with an item.
	 * 
	 * @param player the player
	 * @param block  the clicked block
	 * @param stack  the used item stack
	 * @param event  the fired event
	 * @return whether the right click was consumed and the event should be cancelled
	 */
	default boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		return !player.isSneaking();
	}


	/**
	 * Get's executed when the player clicks with the left mouse button on a from this class handled block with an item.
	 * 
	 * @param player the player
	 * @param block  the clicked block
	 * @param stack  the used item stack
	 * @param event  the fired event
	 * @return whether the left click was consumed and the event should be cancelled
	 */
	default boolean onBlockLeftClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		return false;
	}


	/**
	 * Get's executed when the player interacts physically (like jumping) with a from this class handled block.
	 * 
	 * @param player the player
	 * @param block  the block
	 * @param event  the fired event
	 * @return whether the interaction was consumed and the event should be cancelled
	 */
	default boolean onBlockInteract(Player player, Block block, PlayerInteractEvent event) {
		return false;
	}


	/**
	 * Get's executed when the redstone current at the given block changes.
	 * 
	 * @param block      the block
	 * @param oldCurrent the old redstone current
	 * @param newCurrent the new redstone current
	 * @param event      the fired event
	 */
	@Deprecated
	default void onRedstoneChange(Block block, int oldCurrent, int newCurrent, BlockRedstoneEvent event) {}


	/**
	 * Get's called randomly, similar to the usual minecraft random ticks.
	 * 
	 * @param block the randomly ticked block
	 */
	default void onRandomTick(Block block) {}


	/**
	 * Get's executed when a from this class handled block grows.
	 * 
	 * @param block    the block grown
	 * @param newState the new state of the block
	 * @param event    the fired event
	 * @return whether the growth was successful and the event should not be cancelled
	 */
	default boolean onBlockGrow(Block block, BlockState newState, BlockGrowEvent event) {
		return false;
	}


	/**
	 * Get's executed when a from this class handled block grows a structure.
	 * 
	 * @param block the block grown
	 * @param event the fired event
	 * @return whether the growth was successful and the event should not be cancelled
	 */
	default boolean onBlockStructureGrow(Block block, StructureGrowEvent event) {
		return false;
	}


	/**
	 * Get's executed when a from this class handled leave decays.
	 * 
	 * @param block the block decayed
	 * @param event the fired event
	 * @return whether the decay was successful and the event should not be cancelled
	 */
	default boolean onLeavesDecay(Block block, LeavesDecayEvent event) {
		return true;
	}

}
