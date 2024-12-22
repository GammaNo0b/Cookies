
package me.gamma.cookies.object.block.network;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;

import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.block.BlockTicker;
import me.gamma.cookies.object.block.FilterBlock;



public abstract class AbstractCable<T, F extends Filter<T>> implements BlockTicker, FilterBlock<T, F> {

	private final Set<Location> locations = new HashSet<>();

	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public long getDelay() {
		return 1;
	}

}
