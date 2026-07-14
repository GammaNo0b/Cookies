
package me.gamma.cookies.object.block.network;


import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.block.Cartesian;
import me.gamma.cookies.object.tile.network.AbstractStorageInterface;



public abstract class AbstractStorageInterfaceBlock<R, B extends AbstractStorageInterfaceBlock<R, B, T>, T extends AbstractStorageInterface<R, ? extends Filter<R>, T, B>> extends AbstractStorageComponentBlock<R, B, T> implements NetworkInterfaceBlock<R>, Cartesian {

	/**
	 * Returns the title of the filter gui.
	 * 
	 * @return the title.
	 */
	public abstract String getFilterTitle();

}
