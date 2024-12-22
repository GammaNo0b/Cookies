
package me.gamma.cookies.object.block;


import org.bukkit.block.TileState;

import me.gamma.cookies.object.Filter;



public interface AdvancedFilterBlock<T, F extends Filter<T>> extends FilterBlock<T, F> {

	/**
	 * Returns the priority of the filter at the given block.
	 * 
	 * @param block the block
	 * @return the priority
	 */
	int getPriority(TileState block);

	/**
	 * Sets the specified priority for the filter at the given block.
	 * 
	 * @param block    the block
	 * @param priority the priority
	 */
	void setPriority(TileState block, int priority);

	/**
	 * Returns the channel of the filter at the given block.
	 * 
	 * @param block the block
	 * @return the channel
	 */
	int getChannel(TileState block);

	/**
	 * Sets the specified channel for the filter at the given block.
	 * 
	 * @param block   the block
	 * @param channel the channel
	 */
	void setChannel(TileState block, int channel);

}
