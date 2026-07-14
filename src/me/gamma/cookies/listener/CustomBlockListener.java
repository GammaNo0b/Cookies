
package me.gamma.cookies.listener;


import java.util.Iterator;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.CustomBlockStorage;
import me.gamma.cookies.util.ItemUtils;



public class CustomBlockListener implements Listener {

	/**
	 * Fires whenever a block is placed.
	 * 
	 * @param event {@link BlockPlaceEvent}
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	private void onBlockPlaceEvent(BlockPlaceEvent event) {
		if(event.isCancelled())
			return;

		if(!event.canBuild())
			return;

		ItemStack stack = event.getItemInHand();
		if(!ItemUtils.isCustomItem(stack))
			return;

		Block placed = event.getBlockPlaced();
		AbstractCustomBlock block = Blocks.getCustomBlockFromStack(stack);
		if(block == null)
			return;

		Player player = event.getPlayer();
		if(!block.place(placed, player, event))
			event.setCancelled(true);
	}


	/**
	 * Fires whenever a block get's broken.
	 * 
	 * @param event {@link BlockBreakEvent}
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	private void onBlockBreakEvent(BlockBreakEvent event) {
		if(event.isCancelled())
			return;

		Player player = event.getPlayer();
		Block broken = event.getBlock();

		// check for custom block
		AbstractCustomBlock block = CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(broken);
		if(block != null) {
			// fire Block Break Event for Skull Block and receive the loot
			event.setCancelled(true);
			if(block.onBlockBreak(player, broken, event))
				// break block
				block.breakBlock(broken);
		}
	}


	/**
	 * Fires whenever a block get's exploded due to another block.
	 * 
	 * @param event {@link BlockExplodeEvent}
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	private void onBlockExplode(BlockExplodeEvent event) {
		// iterate over all blocks that are going to 'splode
		Iterator<Block> iterator = event.blockList().iterator();
		while(iterator.hasNext()) {
			Block exploded = iterator.next();
			// check for custom block
			AbstractCustomBlock block = CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(exploded);
			if(block != null) {
				// cancel explosion of custom blocks
				iterator.remove();
				if(block.onBlockExplodesBlock(exploded, event))
					// break block
					block.breakBlock(exploded);
			}
		}
	}


	/**
	 * Fires whenever a block get's exploded due to an entity.
	 * 
	 * @param event {@link EntityExplodeEvent}
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	private void onEntityExplode(EntityExplodeEvent event) {
		// iterate over all blocks that are going to explode
		Iterator<Block> iterator = event.blockList().iterator();
		while(iterator.hasNext()) {
			Block exploded = iterator.next();
			// check for custom block
			AbstractCustomBlock block = CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(exploded);
			if(block != null) {
				// cancel explosion of custom blocks
				iterator.remove();
				if(block.onEntityExplodesBlock(exploded, event))
					// break block
					block.breakBlock(exploded);
			}
		}
	}


	/**
	 * Fires whenever a player interacts with a block.
	 * 
	 * @param event {@link PlayerInteractEvent}
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	private void onBlockInteractEvent(PlayerInteractEvent event) {
		if(event.getHand() == EquipmentSlot.OFF_HAND)
			return;

		Player player = event.getPlayer();
		Block clicked = event.getClickedBlock();
		ItemStack stack = event.getItem();
		Action action = event.getAction();

		// clicked at block
		if(clicked != null) {
			// check for custom block
			AbstractCustomBlock block = CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(clicked);
			if(block != null) {
				if(action == Action.RIGHT_CLICK_BLOCK) {
					// fire Right Click Block Event
					if(block.onBlockRightClick(player, clicked, stack, event))
						event.setCancelled(true);
				} else if(action == Action.LEFT_CLICK_BLOCK) {
					// fire Left Click Block Event
					if(block.onBlockLeftClick(player, clicked, stack, event))
						event.setCancelled(true);
				} else if(action == Action.PHYSICAL) {
					// fire Interact Block Event
					if(block.onBlockInteract(player, clicked, event))
						event.setCancelled(true);
				}
			}
		}
	}


	/**
	 * Fires whenever the redstone changes at a block.
	 * 
	 * @deprecated Only applies to redstone components.
	 * 
	 * @param event {@link BlockRedstoneEvent}
	 */
	@Deprecated
	@EventHandler
	private void onBlockRedstoneEvent(BlockRedstoneEvent event) {
		Block block = event.getBlock();
		AbstractCustomBlock custom = CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(block);
		if(custom != null)
			custom.onRedstoneChange(block, event.getOldCurrent(), event.getNewCurrent(), event);
	}


	/**
	 * Fires when a block grows.
	 * 
	 * @param event the {@link BlockGrowEvent}
	 */
	@EventHandler
	private void onBlockGrow(BlockGrowEvent event) {
		Block block = event.getBlock();
		AbstractCustomBlock custom = CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(block);
		if(custom != null)
			if(!custom.onBlockGrow(block, event.getNewState(), event))
				event.setCancelled(true);
	}


	/**
	 * Fires when a structure grows.
	 * 
	 * @param event the {@link StructureGrowEvent}
	 */
	@EventHandler
	private void onStructureGrow(StructureGrowEvent event) {
		Block block = event.getLocation().getBlock();
		AbstractCustomBlock custom = CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(block);
		if(custom != null)
			if(!custom.onBlockStructureGrow(block, event))
				event.setCancelled(true);
	}


	@EventHandler
	private void onLeavesDecay(LeavesDecayEvent event) {
		Block block = event.getBlock();
		AbstractCustomBlock custom = CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(block);
		if(custom != null)
			if(!custom.onLeavesDecay(block, event))
				event.setCancelled(true);
	}


	/**
	 * Fires when liquids are flowing or dragon eggs are teleporting.
	 * 
	 * @param event {@link BlockFromToEvent}
	 */
	@EventHandler
	private void onBlockChange(BlockFromToEvent event) {
		if(CustomBlockStorage.BLOCK_STORAGE.isCustomBlock(event.getToBlock()))
			event.setCancelled(true);
	}


	/**
	 * Fires when a hopper searches for a inventory to extract or insert items from or to.
	 * 
	 * @param event the {@link HopperInventorySearchEvent}
	 */
	@EventHandler
	private void onHopperInventorySearch(HopperInventorySearchEvent event) {
		// TODO redirect to gui providers
		if(CustomBlockStorage.BLOCK_STORAGE.isCustomBlock(event.getSearchBlock()))
			event.setInventory(null);
	}

	@EventHandler
	private void onBlockPick(PickEve e) {
		
	}
	
}
