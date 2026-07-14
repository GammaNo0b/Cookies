
package me.gamma.cookies.object.gui;


import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;



public interface InventoryHandler {

	/**
	 * Get's executed when a player interacted with the main inventory handled by this instance. Returns whether the event should be cancelled or not.
	 * 
	 * @param player the player
	 * @param gui    the gui inventory
	 * @param event  the fired event
	 * @return if the event should get cancelled
	 */
	default boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		return false;
	}


	/**
	 * Get's executed when a player interacted with the player inventory while viewing the gui handled by this instance. Returns whether the event should
	 * be cancelled or not.
	 * 
	 * @param player the player
	 * @param gui    the player inventory
	 * @param event  the fired event
	 * @return if the event should get cancelled
	 */
	default boolean onPlayerInventoryInteract(Player player, PlayerInventory gui, InventoryClickEvent event) {
		return false;
	}


	/**
	 * Get's executed when a player closes the gui handled by this instance. Returns whether the event should be cancelled or not. The event cannot be
	 * cancelled directly, instead the inventory get's opened again.
	 * 
	 * @param player the player
	 * @param gui    the gui inventory
	 * @param event  the fired event
	 * @return if the event should be cancelled
	 */
	default boolean onInventoryClose(Player player, Inventory gui, InventoryCloseEvent event) {
		return false;
	}

}
