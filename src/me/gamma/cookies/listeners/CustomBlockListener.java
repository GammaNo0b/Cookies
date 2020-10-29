
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

import me.gamma.cookies.objects.block.AbstractTileStateBlock;
import me.gamma.cookies.objects.block.GuiProvider;
import me.gamma.cookies.objects.block.skull.ClownfishChest;
import me.gamma.cookies.objects.block.skull.StorageMonitor;
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
			// iterate over all registered Skull Blocks
			for(AbstractTileStateBlock block : CustomBlockSetup.customBlocks) {
				// check if item has same ID as current Skull Block
				if(block.isInstanceOf(item.getItemMeta())) {
					// fire Block Place Event for Skull Block
					block.onBlockPlace(event.getPlayer(), item, state, event);
				}
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
			// iterate over all registered Skull Blocks
			for(AbstractTileStateBlock block : CustomBlockSetup.customBlocks) {
				// check if skull has same ID as current Skull Block
				if(block.isInstanceOf(state)) {
					// fire Block Break Event for Skull Block and receive the loot
				// fire Block Break Event for Skull Block and receive the loot
					ItemStack drop = block.onBlockBreak(event.getPlayer(), state, event);
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
		// iterate over all registered Skull Blocks
		for(AbstractTileStateBlock block : CustomBlockSetup.customBlocks) {
			if(event.getClickedBlock() != null) {
				// block is not null
				// check if placed block is a skull
				if(event.getClickedBlock().getState() instanceof TileState) {
					TileState skull = (TileState) event.getClickedBlock().getState();
					// check if skull has same ID as current Skull Block
					if(block.isInstanceOf(skull)) {
						if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
							// fire Right Click Block Event
							block.onBlockRightClick(event.getPlayer(), skull, event);
						} else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
							// fire Left Click Block Event
							block.onBlockLeftClick(event.getPlayer(), skull, event);
						}
					}
				}
			}
			ItemStack stack = event.getItem();
			if(stack != null) {
				// check if item has same ID as current Skull Block
				if(block.isInstanceOf(stack.getItemMeta())) {
					if(event.getAction() == Action.RIGHT_CLICK_AIR) {
						// fire Right Click Air Event
						block.onAirRightClick(event.getPlayer(), event.getItem(), event);
					} else if(event.getAction() == Action.LEFT_CLICK_AIR) {
						// fire Left Click Air Event
						block.onAirLeftClick(event.getPlayer(), event.getItem(), event);
					} else if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
						// fire Right Click Block Event
						block.onBlockRightClick(event.getPlayer(), event.getItem(), event);
					} else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
						// fire Left Click Block Event
						block.onBlockLeftClick(event.getPlayer(), event.getItem(), event);
					}
				}
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
						ItemStack identifier = event.getInventory().getItem(guiBlock.getIdentifierSlot());
						int[] pos = new int[3];
						String[] strings = identifier.getItemMeta().getDisplayName().substring(14).split(" : ");
						for(int i = 0; i < pos.length; i++) {
							try {
								pos[i] = Integer.parseInt(strings[i]);
							} catch(NumberFormatException e) {
								pos[i] = 0;
							}
						}

						Block block = clicker.getWorld().getBlockAt(new Location(clicker.getWorld(), pos[0], pos[1], pos[2]));
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
							event.setCancelled(true);
							return;
						}
						int[] pos = new int[3];
						String[] strings = identifier.getItemMeta().getDisplayName().substring(14).split(" : ");
						for(int i = 0; i < pos.length; i++) {
							try {
								pos[i] = Integer.parseInt(strings[i]);
							} catch(NumberFormatException e) {
								pos[i] = 0;
							}
						}

						Block block = clicker.getWorld().getBlockAt(new Location(clicker.getWorld(), pos[0], pos[1], pos[2]));
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
	 * fires whenever the player interacts with an inventory
	 * 
	 * @param event {@link InventoryClickEvent}
	 */
	@EventHandler
	public void onSpecificInventoryClickEvent(InventoryClickEvent event) {
		// check if clicker is player (should be always the case)
		if(event.getWhoClicked() instanceof Player) {
			// switch between the title of the inventory
			switch (event.getView().getTitle()) {
				case StorageMonitor.STATS_TITLE:
					// Storage Monitor Statistics Inventory
					event.setCancelled(true);
					break;
				case ClownfishChest.BACKPACK_TITLE:
					// Clownfish Backpack Inventory
					if(event.getClickedInventory() instanceof PlayerInventory) {
						event.setCancelled(CustomBlockSetup.CLOWNFISH_CHEST.onBackpackPlayerInventoryInteract((Player) event.getWhoClicked(), event.getCurrentItem(), event.getClickedInventory(), event));
					} else {
						event.setCancelled(CustomBlockSetup.CLOWNFISH_CHEST.onBackpackGuiInteract((Player) event.getWhoClicked(), event.getCurrentItem(), event.getClickedInventory(), event));
					}
					break;
				default:
					break;
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
					ItemStack identifier = event.getInventory().getItem(guiBlock.getIdentifierSlot());
					if(identifier == null) {
						return;
					}
					int[] pos = new int[3];
					String[] strings = identifier.getItemMeta().getDisplayName().substring(14).split(" : ");
					for(int i = 0; i < pos.length; i++) {
						try {
							pos[i] = Integer.parseInt(strings[i]);
						} catch(NumberFormatException e) {
							pos[i] = 0;
						}
					}

					Block block = player.getWorld().getBlockAt(new Location(player.getWorld(), pos[0], pos[1], pos[2]));
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


	/**
	 * fires whenever an inventory get's closed
	 * 
	 * @param event {@link InventoryCloseEvent}
	 */
	@EventHandler
	public void onSpecificInventoryCloseEvent(InventoryCloseEvent event) {
		// check if clicker is player (should be always the case)
		if(event.getPlayer() instanceof Player) {
			Player player = (Player) event.getPlayer();
			// switch between the title of the inventory
			switch (event.getView().getTitle()) {
				case ClownfishChest.BACKPACK_TITLE:
					// Clownfish Backpack Inventory
					CustomBlockSetup.CLOWNFISH_CHEST.onBackpackClose(player, event.getPlayer().getInventory().getItemInMainHand(), event.getInventory(), event);
					break;
			}
		}
	}

}
