
package me.gamma.cookies.object;


import org.bukkit.Bukkit;

import me.gamma.cookies.Cookies;



public interface CustomRunnable {

	default void register() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Cookies.INSTANCE, this.getCustomTask(), 0, this.getDelay());
	}


	Runnable getCustomTask();

	long getDelay();

}
