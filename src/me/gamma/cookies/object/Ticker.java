
package me.gamma.cookies.object;


import java.util.HashMap;

import org.bukkit.Bukkit;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.init.Registry;



public interface Ticker {

	/**
	 * Stores all registered instances of this class with their task id if they are currently running.
	 */
	Registry<Ticker> TICKERS = new Registry<>();

	HashMap<Ticker, Integer> taskids = new HashMap<>();

	/**
	 * Executes one tick of the Ticker.
	 */
	void tick();

	/**
	 * The length of the period in which instances of this block should be ticked.
	 * 
	 * @return the length of the period
	 */
	long getDelay();


	/**
	 * Registers this ticker.
	 */
	default void registerTicker() {
		TICKERS.register(this);
	}


	/**
	 * Starts this ticker..
	 */
	default void start() {
		taskids.put(this, Bukkit.getScheduler().scheduleSyncRepeatingTask(Cookies.INSTANCE, this::tick, this.getDelay(), this.getDelay()));
	}


	/**
	 * Stops this task.
	 */
	default void stop() {
		Integer id = taskids.get(this);
		if(id != null)
			Bukkit.getScheduler().cancelTask(id);
	}


	/**
	 * Starts ticking the stored instances.
	 */
	public static void startTicking() {
		TICKERS.forEach(Ticker::start);
	}


	/**
	 * Stops the running instances.
	 */
	public static void stopTicking() {
		TICKERS.forEach(Ticker::stop);
	}

}
