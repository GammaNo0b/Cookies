
package me.gamma.cookies.object;


/**
 * Class to filter the given resource of the same type.
 * 
 * @author gamma
 *
 * @param <T> type of the resource to be filtered
 */
public interface Filter<T> {

	/**
	 * Returns how much resources of the given type and the given amount can go through this filter.
	 * 
	 * @param type   the type
	 * @param amount the amount
	 * @return the amount of let through resources
	 */
	int filter(T type, int amount);


	/**
	 * Returns an empty filter that will allow every resource.
	 * 
	 * @param <T> the type of the filter
	 * @return the empty filter
	 */
	static <T> Filter<T> empty() {
		return (_, amount) -> amount;
	}


	/**
	 * Returns a new filter that filters items matching this filter and the given filter.
	 * 
	 * @param other the second filter
	 * @return the new filter
	 */
	default Filter<T> and(Filter<T> other) {
		return (type, amount) -> other.filter(type, this.filter(type, amount));
	}

}
