
package me.gamma.cookies.object.block;


import me.gamma.cookies.object.Filter;



public interface AdvancedFilterBlock<T, F extends Filter<T>> extends FilterBlock<T, F> {

	/**
	 * Returns the priority of the filter at this block.
	 * 
	 * @return the priority
	 */
	int getPriority();

	/**
	 * Sets the specified priority for the filter at this block.
	 * 
	 * @param priority the priority
	 */
	void setPriority(int priority);

	/**
	 * Returns the channel of the filter at this block.
	 * 
	 * @return the channel
	 */
	int getChannel();

	/**
	 * Sets the specified channel for the filter at this block.
	 * 
	 * @param channel the channel
	 */
	void setChannel(int channel);

}
