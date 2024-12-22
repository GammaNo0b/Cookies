
package me.gamma.cookies.listener;


import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.util.BlockUtils;
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
		if(!(placed.getState() instanceof TileState state))
			return;

		ItemMeta meta = stack.getItemMeta();
		AbstractCustomBlock block = Blocks.getCustomBlockFromHolder(meta);
		if(block == null)
			return;

		Player player = event.getPlayer();
		if(!block.canPlace(player, placed) || block.onBlockPlace(player, meta, state))
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

		// check if placed block is a skull
		if(broken.getState() instanceof TileState state) {
			AbstractCustomBlock block = Blocks.getCustomBlockFromBlock(state);
			if(block != null) {
				// fire Block Break Event for Skull Block and receive the loot
				event.setCancelled(true);
				if(!block.onBlockBreak(player, state, event))
					broken.setType(Material.AIR);
			}
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
			if(exploded.getState() instanceof TileState state) {
				// exploded block is custom block
				AbstractCustomBlock block = Blocks.getCustomBlockFromBlock(state);
				if(block != null) {
					if(block.onBlockExplodesBlock(state, event))
						// remove block from exploding if custom block
						iterator.remove();
				}
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
			if(exploded.getState() instanceof TileState state) {
				// exploded block is custom block
				AbstractCustomBlock block = Blocks.getCustomBlockFromBlock(state);
				if(block != null) {
					if(block.onEntityExplodesBlock(state, event))
						// remove block from exploding if custom block
						iterator.remove();
				}
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
			if(clicked.getState() instanceof TileState state) {
				AbstractCustomBlock block = Blocks.getCustomBlockFromBlock(state);
				if(block != null) {
					if(action == Action.RIGHT_CLICK_BLOCK) {
						// fire Right Click Block Event
						if(block.onBlockRightClick(player, state, stack, event))
							event.setCancelled(true);
					} else if(action == Action.LEFT_CLICK_BLOCK) {
						// fire Left Click Block Event
						if(block.onBlockLeftClick(player, state, stack, event))
							event.setCancelled(true);
					} else if(action == Action.PHYSICAL) {
						// fire Interact Block Event
						if(block.onBlockInteract(player, state, event))
							event.setCancelled(true);
					}
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
	public void onBlockRedstoneEvent(BlockRedstoneEvent event) {
		Block block = event.getBlock();
		if(!(block.getState() instanceof TileState state))
			return;

		AbstractCustomBlock custom = Blocks.getCustomBlockFromBlock(state);
		if(custom != null)
			custom.onRedstoneChange(state, event.getOldCurrent(), event.getNewCurrent(), event);
	}


	/**
	 * Fires when liquids are flowing or dragon eggs are teleporting.
	 * 
	 * @param event {@link BlockFromToEvent}
	 */
	@EventHandler
	public void onBlockChange(BlockFromToEvent event) {
		if(BlockUtils.isCustomBlock(event.getToBlock()))
			event.setCancelled(true);
	}

}
