
package me.gamma.cookies.object.block;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;



public abstract class AbstractWorkBlock extends AbstractCustomBlock implements BlockTicker {

	private final Set<Location> locations = new HashSet<>();

	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public long getDelay() {
		return this.getWorkPeriod();
	}


	/**
	 * Returns the length in ticks of a work period.
	 * 
	 * @return the length
	 */
	protected abstract int getWorkPeriod();

}
