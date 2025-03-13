
package me.gamma.cookies.object;


import org.bukkit.Bukkit;

import me.gamma.cookies.Cookies;



public interface CustomRunnable {

	/**
	 * Registeres this runnable to the bukket scheduler.
	 */
	default void register() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Cookies.INSTANCE, this.getCustomTask(), 0, this.getDelay());
	}


	/**
	 * Returns the task to be executed.
	 * 
	 * @return the executable
	 */
	Runnable getCustomTask();

	/**
	 * Returns the delay between sucsessive executions.
	 * 
	 * @return the delay
	 */
	long getDelay();

}
