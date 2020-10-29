
package me.gamma.cookies.objects.block;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.TileState;
import org.bukkit.scheduler.BukkitScheduler;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.setup.CustomBlockSetup;



public interface BlockTicker extends BlockRegister {

	void tick(TileState block);

	long getDelay();
	
	boolean shouldTick(TileState block);
	
	
	default void tick() {
		for(Location location : this.getLocations()) {
			if(location.getBlock().getState() instanceof TileState) {
				TileState block = (TileState) location.getBlock().getState();
				if(this.shouldTick(block)) {
					this.tick(block);
				}
			}
		}
	}


	default void initialize(BukkitScheduler scheduler) {
		scheduler.scheduleSyncRepeatingTask(Cookies.INSTANCE, this::tick, this.getDelay(), this.getDelay());
	}


	public static void startTicking() {
		CustomBlockSetup.blockTickers.forEach(ticker -> Bukkit.getScheduler().scheduleSyncRepeatingTask(Cookies.INSTANCE, ticker::tick, ticker.getDelay(), ticker.getDelay()));
	}

}
