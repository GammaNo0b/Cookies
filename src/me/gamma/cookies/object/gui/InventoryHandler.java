
package me.gamma.cookies.object.gui;


import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemUtils;



public interface InventoryHandler {

	String KEY_IDENTIFIIER = "inv_id";

	/**
	 * Returns the unique identifier.
	 * 
	 * @return the identifier
	 */
	String getIdentifier();

	/**
	 * Returns the slot index from the slot in which the stack should store the location of the inventory holder block.
	 * 
	 * @return the slot index
	 */
	int getIdentifierSlot();


	/**
	 * Marks the given inventory so that it can be detected by this handler later on.
	 * 
	 * @param inventory the inventory
	 */
	default void markInventory(Inventory inventory) {
		InventoryUtils.storeStringInStack(inventory.getItem(this.getIdentifierSlot()), KEY_IDENTIFIIER, this.getIdentifier());
	}


	/**
	 * Checks if the given inventory should be handled by this inventory handler.
	 * 
	 * @param inventory the inventory
	 * @return if the inventory should be handled
	 */
	default boolean check(Inventory inventory) {
		int slot = this.getIdentifierSlot();

		if(inventory.getSize() <= slot)
			return false;

		ItemStack stack = inventory.getItem(slot);
		if(ItemUtils.isEmpty(stack))
			return false;

		String identifier = InventoryUtils.getStringFromStack(stack, KEY_IDENTIFIIER);
		return this.getIdentifier().equals(identifier);
	}


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
