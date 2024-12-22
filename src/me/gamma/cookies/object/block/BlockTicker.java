
package me.gamma.cookies.object.block;


import org.bukkit.Location;
import org.bukkit.block.TileState;

import me.gamma.cookies.object.Ticker;



public interface BlockTicker extends TileBlockRegister, Ticker {

	/**
	 * Get's executed every tick for the given block.
	 * 
	 * @param block the block
	 */
	void tick(TileState block);


	@Override
	default void tick() {
		for(Location location : this.getLocations())
			if(location.getBlock().getState() instanceof TileState state)
				this.tick(state);
	}


	@Override
	default void register() {
		TileBlockRegister.super.register();
		Ticker.super.registerTicker();
	}

}
