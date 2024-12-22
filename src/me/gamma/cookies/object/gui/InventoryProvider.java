
package me.gamma.cookies.object.gui;


import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.object.gui.task.InventoryTask;
import me.gamma.cookies.object.gui.task.StaticInventoryTask;
import me.gamma.cookies.util.InventoryUtils;



public interface InventoryProvider<D> extends InventoryHandler {

	/**
	 * Stores the given data in the given inventory to later retrieve it back from an inventory.
	 * 
	 * @param inventory the inventory
	 * @param data      the data
	 */
	default void storeData(Inventory inventory, D data) {}


	/**
	 * Fetches the data from the given inventory to retrieve the data from an inventory or player.
	 * 
	 * @param inventory the inventory
	 * 
	 * @return the data
	 */
	default D fetchData(Inventory inventory) {
		return null;
	}


	/**
	 * Returns the title of the inventory.
	 * 
	 * @param the data this provider requires
	 * @return the title
	 */
	String getTitle(D data);

	/**
	 * Returns the amount of rows the gui should have
	 * 
	 * @return the amound of rows
	 */
	int rows();


	/**
	 * Returns the sound that get played when opening the gui.
	 * 
	 * @return the opening sound
	 */
	default Sound getSound() {
		return null;
	}


	/**
	 * Returns the sound category of which the sound played is part of.
	 * 
	 * @return the sound category
	 */
	default SoundCategory getSoundCategory() {
		return SoundCategory.MASTER;
	}


	/**
	 * Creates a new gui for the given data.
	 * 
	 * @param data the data
	 * @return the newly created gui
	 */
	default Inventory createGui(D data) {
		return InventoryUtils.createBasicInventoryProviderGui(this, data);
	}


	/**
	 * Returns the inventory instance for the given data.
	 * 
	 * @param data the data
	 * @return the inventory instance
	 */
	default Inventory getGui(D data) {
		return this.createGui(data);
	}


	/**
	 * Creates the inventory task to dynamically change the inventory.
	 * 
	 * @param inventory the inventory
	 * @param data      the data
	 * @return the inventory task
	 */
	default InventoryTask createInventoryTask(Inventory inventory, D data) {
		return new StaticInventoryTask(inventory);
	}


	/**
	 * Opens the gui for the given player from the given data.
	 * 
	 * @param player the player
	 * @param item   the item
	 */
	default void openGui(HumanEntity player, D data) {
		this.openGui(player, data, true, true);
	}


	/**
	 * Opens the gui for the given player from the given data.
	 * 
	 * @param player    the player
	 * @param data      the data
	 * @param history   whether the inventory should be added to the query que
	 * @param playsound wether the player should hear the opening sound
	 */
	default void openGui(HumanEntity player, D data, boolean history, boolean playsound) {
		if(playsound && player instanceof Player p)
			p.playSound(player.getLocation(), this.getSound(), this.getSoundCategory(), 0.8F, 1.0F);
		Inventory gui = this.getGui(data);
		this.storeData(gui, data);
		this.markInventory(gui);
		InventoryTask task = this.createInventoryTask(gui, data);
		if(history)
			History.proceed(player, task);
		else
			History.update(player, task);
	}


	@Override
	default boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		return this.onMainInventoryInteract(player, this.fetchData(gui), gui, event);
	}


	@Override
	default boolean onPlayerInventoryInteract(Player player, PlayerInventory gui, InventoryClickEvent event) {
		return this.onPlayerInventoryInteract(player, this.fetchData(event.getInventory()), gui, event);
	}


	@Override
	default boolean onInventoryClose(Player player, Inventory gui, InventoryCloseEvent event) {
		return this.onInventoryClose(player, this.fetchData(gui), gui, event);
	}


	/**
	 * Get's executed when a player interacted with the main inventory handled by this instance. Returns whether the event should be cancelled or not.
	 * 
	 * @param player the player
	 * @param gui    the gui inventory
	 * @param data   the data
	 * @param event  the fired event
	 * @return if the event should get cancelled
	 */
	default boolean onMainInventoryInteract(Player player, D data, Inventory gui, InventoryClickEvent event) {
		return false;
	}


	/**
	 * Get's executed when a player interacted with the player inventory while viewing the gui handled by this instance. Returns whether the event should
	 * be cancelled or not.
	 * 
	 * @param player the player
	 * @param gui    the player inventory
	 * @param data   the data
	 * @param event  the fired event
	 * @return if the event should get cancelled
	 */
	default boolean onPlayerInventoryInteract(Player player, D data, PlayerInventory gui, InventoryClickEvent event) {
		return false;
	}


	/**
	 * Get's executed when a player closes the gui handled by this instance. Returns whether the event should be cancelled or not. The event cannot be
	 * cancelled directly, instead the inventory get's opened again.
	 * 
	 * @param player the player
	 * @param gui    the gui inventory
	 * @param data   the data
	 * @param event  the fired event
	 * @return if the event should be cancelled
	 */
	default boolean onInventoryClose(Player player, D data, Inventory gui, InventoryCloseEvent event) {
		return false;
	}

}
