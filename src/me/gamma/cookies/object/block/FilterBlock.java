
package me.gamma.cookies.object.block;


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
	 * @return the filter
	 */
	F getFilter();

	/**
	 * Replaces the current filter of this block with the given one.
	 * 
	 * @param filter the filter
	 */
	void setFilter(F filter);

}
