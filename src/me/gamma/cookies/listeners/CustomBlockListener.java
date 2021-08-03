
package me.gamma.cookies.listeners;


import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.managers.InventoryManager;
import me.gamma.cookies.objects.block.AbstractTileStateBlock;
import me.gamma.cookies.objects.block.GuiProvider;
import me.gamma.cookies.setup.CustomBlockSetup;



public class CustomBlockListener implements Listener {

	/**
	 * Fires whenever a block get's placed.
	 * 
	 * @param event {@link BlockPlaceEvent}
	 */
	@EventHandler
	private void onBlockPlaceEvent(BlockPlaceEvent event) {
		ItemStack item = event.getItemInHand();
		if(event.getBlockPlaced().getState() instanceof TileState) {
			TileState state = (TileState) event.getBlockPlaced().getState();
			AbstractTileStateBlock block = CustomBlockSetup.getCustomBlockFromStack(item);
			if(block != null) {
				block.onBlockPlace(event.getPlayer(), item, state, event);
			}
		}
	}


	/**
	 * Fires whenever a block get's broken.
	 * 
	 * @param event {@link BlockBreakEvent}
	 */
	@EventHandler
	private void onBlockBreakEvent(BlockBreakEvent event) {
		// check if placed block is a skull
		if(event.getBlock().getState() instanceof TileState) {
			TileState state = (TileState) event.getBlock().getState();
			AbstractTileStateBlock block = CustomBlockSetup.getCustomBlockFromTileState(state);
			if(block != null) {
				// fire Block Break Event for Skull Block and receive the loot
				ItemStack drop = block.onBlockBreak(event.getPlayer(), state, event);
				if(!event.isCancelled()) {
					// give Drop to Player if he is in Survival
					if(event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
						event.getBlock().getWorld().dropItem(event.getBlock().getLocation().add(0.5D, 0.5D, 0.5D), drop);
					}
					event.getBlock().setType(Material.AIR);
					event.setCancelled(true);
				}
			}
		}
	}


	/**
	 * fires whenever a player interacts with a block
	 * 
	 * @param event {@link PlayerInteractEvent}
	 */
	@EventHandler
	private void onBlockInteractEvent(PlayerInteractEvent event) {
		// block is not null
		if(event.getClickedBlock() != null) {
			if(event.getClickedBlock().getState() instanceof TileState) {
				TileState state = (TileState) event.getClickedBlock().getState();
				AbstractTileStateBlock block = CustomBlockSetup.getCustomBlockFromTileState(state);
				if(block != null) {
					if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
						// fire Right Click Block Event
						if(block.onBlockRightClick(event.getPlayer(), state, event))
							event.setCancelled(true);
					} else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
						// fire Left Click Block Event
						if(block.onBlockLeftClick(event.getPlayer(), state, event))
							event.setCancelled(true);
					}
				}
			}
		}
		AbstractTileStateBlock block = CustomBlockSetup.getCustomBlockFromStack(event.getItem());
		if(block != null) {
			if(event.getAction() == Action.RIGHT_CLICK_AIR) {
				// fire Right Click Air Event
				if(block.onAirRightClick(event.getPlayer(), event.getItem(), event))
					event.setCancelled(true);
			} else if(event.getAction() == Action.LEFT_CLICK_AIR) {
				// fire Left Click Air Event
				if(block.onAirLeftClick(event.getPlayer(), event.getItem(), event))
					event.setCancelled(true);
			} else if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				// fire Right Click Block Event
				if(block.onBlockRightClick(event.getPlayer(), event.getItem(), event))
					event.setCancelled(true);
			} else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
				// fire Left Click Block Event
				if(block.onBlockLeftClick(event.getPlayer(), event.getItem(), event))
					event.setCancelled(true);
			}
		}
	}


	/**
	 * fires whenever the player interacts with an inventory
	 * 
	 * @param event {@link InventoryClickEvent}
	 */
	@EventHandler
	private void onInventoryClickEvent(InventoryClickEvent event) {
		// check if clicker is player (should be always the case)
		if(event.getWhoClicked() instanceof Player) {
			Player clicker = (Player) event.getWhoClicked();
			// iterate over all registered Gui Skull Blocks
			for(GuiProvider guiBlock : CustomBlockSetup.guiBlocks) {
				// check if the inventory title corresponds with the skull block's display name
				if(event.getView().getTitle().equals(guiBlock.getDisplayName())) {
					if(event.getClickedInventory() instanceof PlayerInventory) {
						// interacted with player inventory

						// get the location of the skullblock from the location identifier
						Location location = guiBlock.getLocation(clicker.getWorld(), event.getInventory());
						Block block = location.getBlock();
						// check calculated block
						if(block.getState() instanceof TileState) {
							// fire Player Inventory Interact Event
							event.setCancelled(guiBlock.onPlayerInventoryInteract(clicker, (TileState) block.getState(), event.getInventory(), event));
						}
					} else {
						// interacted with skull block inventory

						// get the location of the skullblock from the location identifier
						ItemStack identifier = event.getInventory().getItem(guiBlock.getIdentifierSlot());
						if(identifier == null) {
							return;
						}
						Location location = InventoryManager.getIdentifierLocation(clicker.getWorld(), identifier);
						Block block = location.getBlock();
						// check calculated block
						if(block.getState() instanceof TileState) {
							// fire Main Inventory Interact Event
							event.setCancelled(guiBlock.onMainInventoryInteract(clicker, (TileState) block.getState(), event.getInventory(), event));
						}
					}
				}
			}
		}
	}


	/**
	 * fires whenever an inventory get's closed
	 * 
	 * @param event {@link InventoryCloseEvent}
	 */
	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		// check if clicker is player (should be always the case)
		if(event.getPlayer() instanceof Player) {
			Player player = (Player) event.getPlayer();
			// iterate over all registered Gui Skull Blocks
			for(GuiProvider guiBlock : CustomBlockSetup.guiBlocks) {
				// check if the inventory title corresponds with the skull block's display name
				if(event.getView().getTitle().equals(guiBlock.getDisplayName())) {
					// get the location of the skullblock from the location identifier
					int slot = guiBlock.getIdentifierSlot();
					if(slot >= guiBlock.getRows() * 9) {
						return;
					}
					ItemStack identifier = event.getInventory().getItem(slot);
					if(identifier == null) {
						return;
					}
					Location location = InventoryManager.getIdentifierLocation(player.getWorld(), identifier);
					Block block = location.getBlock();
					// check calculated block
					if(block.getState() instanceof TileState) {
						TileState state = (TileState) block.getState();
						// fire Main Inventory Interact Event
						guiBlock.onInventoryClose(player, state, event.getInventory(), event);
					}
				}
			}
		}
	}

}
