
package me.gamma.cookies.object.gui.task;


import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.Cookies;



public abstract class InventoryTask implements Runnable {

	protected final Inventory inventory;
	protected int cycle;
	protected final long delay;
	protected boolean running;

	/**
	 * Creates a new Inventory Task
	 * 
	 * @param inventory the inventory
	 * @param delay     the delay at which the inventory should be updated.
	 */
	public InventoryTask(Inventory inventory, long delay) {
		this.inventory = inventory;
		this.delay = delay;
	}


	/**
	 * Opens the underlying inventory for the given player.
	 * 
	 * @param player the player
	 */
	public void open(HumanEntity player) {
		player.openInventory(this.inventory);
	}


	/**
	 * Starts the task.
	 */
	public void start() {
		this.running = true;
		this.initInventory();
		this.run();
	}


	/**
	 * Stops the task.
	 */
	public void stop() {
		this.running = false;
	}


	@Override
	public void run() {
		if(!this.running) {
			this.finishInventory();
			return;
		}

		this.updateInventory();
		this.cycle++;

		Bukkit.getScheduler().runTaskLater(Cookies.INSTANCE, this, this.delay);
	}


	/**
	 * Get's executed right after this task is started.
	 */
	protected void initInventory() {}


	/**
	 * Get's executed periodically to update the inventory.
	 */
	protected abstract void updateInventory();


	/**
	 * Get's executed when this task is stopped.
	 */
	protected void finishInventory() {}

}
