
package me.gamma.cookies.listener;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.init.Registries;
import me.gamma.cookies.object.gui.InventoryHandler;



public class CustomInventoryListener implements Listener {

	/**
	 * Get's executed when the player clicks inside an inventory.
	 * 
	 * @param event the {@link InventoryClickEvent}
	 */
	@EventHandler
	public void onInventoryInteract(InventoryClickEvent event) {
		Inventory inventory = event.getInventory();
		InventoryHandler handler = Registries.INVENTORY_HANDLERS.filterFirst(h -> h.check(inventory));
		if(handler == null)
			return;

		if(event.getWhoClicked() instanceof Player player) {
			if(event.getClickedInventory() instanceof PlayerInventory playerinv) {
				if(handler.onPlayerInventoryInteract(player, playerinv, event))
					event.setCancelled(true);
			} else {
				if(handler.onMainInventoryInteract(player, inventory, event))
					event.setCancelled(true);
			}
		}
	}


	/**
	 * Get's executed when the player closes an inventory.
	 * 
	 * @param event the {@link InventoryCloseEvent}
	 */
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		Inventory inventory = event.getInventory();
		InventoryHandler handler = Registries.INVENTORY_HANDLERS.filterFirst(h -> h.check(inventory));
		if(handler != null && event.getPlayer() instanceof Player player)
			if(handler.onInventoryClose(player, inventory, event))
				player.openInventory(inventory);
	}

}
