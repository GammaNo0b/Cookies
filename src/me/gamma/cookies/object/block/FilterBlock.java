
package me.gamma.cookies.object.block;


import org.bukkit.block.TileState;

import me.gamma.cookies.object.Filter;



public interface FilterBlock<T, F extends Filter<T>> {

	/**
	 * Returns the title of the filter gui.
	 * 
	 * @return the title
	 */
	String getFilterTitle();

	/**
	 * Returns the filter of this block.
	 * 
	 * @param block the block
	 * @return the filter
	 */
	F getFilter(TileState block);

	/**
	 * Replaces the current filter of this block with the given one.
	 * 
	 * @param block  the block
	 * @param filter the filter
	 */
	void setFilter(TileState block, F filter);

}
