
package me.gamma.cookies.object.gui;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.entity.HumanEntity;

import me.gamma.cookies.object.gui.task.InventoryTask;



/**
 * Keeps track of the inventories a playr has opened.
 * 
 * @author gamma
 *
 */
public class History {

	private static final HashMap<UUID, LinkedList<InventoryTask>> history = new HashMap<>();

	/**
	 * Returns the list of inventory tasks the player with the given uuid has visited.
	 * 
	 * @param uuid the uuid of the player
	 * @return the history of the player
	 */
	private static LinkedList<InventoryTask> getHistory(UUID uuid) {
		LinkedList<InventoryTask> list = history.get(uuid);
		if(list == null) {
			list = new LinkedList<>();
			history.put(uuid, list);
		}
		return list;
	}


	/**
	 * Returns the currently running inventory task by the given player.
	 * 
	 * @param player the player
	 * @return the currently running inventory task
	 */
	public static InventoryTask getPresent(HumanEntity player) {
		LinkedList<InventoryTask> history = getHistory(player.getUniqueId());
		return history.isEmpty() ? null : history.getLast();
	}


	/**
	 * Adds a new inventory task to the players history and opens the given inventory task.
	 * 
	 * @param player    the player
	 * @param inventory the inventory task
	 */
	public static void proceed(HumanEntity player, InventoryTask task) {
		add(player, task);
		task.open(player);
		task.start();
	}


	/**
	 * Adds a new inventory task to the players history.
	 * 
	 * @param player    the player
	 * @param inventory the inventory task
	 */
	public static void add(HumanEntity player, InventoryTask task) {
		LinkedList<InventoryTask> history = getHistory(player.getUniqueId());
		if(!history.isEmpty())
			history.getLast().stop();
		history.add(task);
	}


	/**
	 * Replaces the current inventory task in the given's player history with the given one.
	 * 
	 * @param player the player
	 * @param task   the new inventory task
	 */
	public static void update(HumanEntity player, InventoryTask task) {
		LinkedList<InventoryTask> history = getHistory(player.getUniqueId());
		if(history.isEmpty())
			return;
		history.removeLast().stop();
		history.add(task);
		task.open(player);
		task.start();
	}


	/**
	 * Removes, returns and opens the last visited inventory task from the given player.
	 * 
	 * @param player the player
	 * @return the last visited inventory task
	 */
	public static InventoryTask travelBack(HumanEntity player) {
		LinkedList<InventoryTask> history = getHistory(player.getUniqueId());
		if(history.isEmpty())
			return null;

		InventoryTask last = history.removeLast();
		last.stop();

		if(!history.isEmpty()) {
			InventoryTask task = history.getLast();
			task.open(player);
			task.start();
		}
		return last;
	}


	/**
	 * Clears and returns the history of the given player.
	 * 
	 * @param player the player
	 * @return the history
	 */
	public static LinkedList<InventoryTask> clear(HumanEntity player) {
		return history.remove(player.getUniqueId());
	}

}
