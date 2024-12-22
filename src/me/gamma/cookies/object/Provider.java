
package me.gamma.cookies.object;


/**
 * Provides an amount of a specific type.
 * 
 * @author gamma
 *
 * @param <T> Type of the provider
 */
public interface Provider<T> {

	/**
	 * Returns the amount of resources stored in this provider.
	 * 
	 * @return the amount
	 */
	int amount();

	/**
	 * Returns the capacity of this provider.
	 * 
	 * @return the maximum amount of resources to be stored
	 */
	int capacity();


	/**
	 * Returns the amount of free space in this provider.
	 * 
	 * @return the free space
	 */
	default int space() {
		return this.capacity() - this.amount();
	}


	/**
	 * Adds the given amount to this provider regardless of it's fullness.
	 * 
	 * @param amount the amount to be added
	 */
	default void add(int amount) {
		this.add(this.getType(), amount);
	}


	/**
	 * Adds the given amount to this provider regardless of it's fullness.
	 * 
	 * @param type   the type
	 * @param amount the amount to be added
	 */
	void add(T type, int amount);

	/**
	 * Removes the given amount from this provider regardless of it's fullness.
	 * 
	 * @param amount the amount to be removed
	 */
	void remove(int amount);


	/**
	 * Stores the given amount in this provider and returns what couldn't be stored.
	 * 
	 * @param amount the amount to be stored
	 * @return the amount that couldn't be stored
	 */
	default int set(int amount) {
		return this.set(this.getType(), amount);
	}


	/**
	 * Stores the given amount in this provider and returns what couldn't be stored.
	 * 
	 * @param type   the type
	 * @param amount the amount to be stored
	 * 
	 * @return the amount that couldn't be stored
	 */
	default int set(T type, int amount) {
		return this.set(type, amount, Filter.empty());
	}


	/**
	 * Stores the given amount in this provider respecting the given filter and returns what couldn't be stored.
	 * 
	 * @param type   the type
	 * @param amount the amount to be stored
	 * @param filter the filter
	 * 
	 * @return the amount that couldn't be stored
	 */
	default int set(T type, int amount, Filter<T> filter) {
		int add = filter.filter(type, Math.min(this.space(), amount));
		this.add(type, add);
		return amount - add;
	}


	/**
	 * Returns between 0 and {@code max}, depending on how much this instance can provide.
	 * 
	 * @param max maximum amount to be removed
	 * @return between 0 and {@code max}
	 */
	default int get(int max) {
		return this.get(max, Filter.empty());
	}


	/**
	 * Returns between 0 and {@code max}, depending on how much this instance can provide so that the filter is satisfied.
	 * 
	 * @param max    the maximum amount to be removed
	 * @param filter the filter for filtering further
	 * @return between 0 and {@code max}
	 */
	default int get(int max, Filter<T> filter) {
		int remove = filter.filter(this.getType(), Math.min(this.amount(), max));
		this.remove(remove);
		return remove;
	}


	/**
	 * Checks if this provider can at least provide <code>amount</code> amount of resources.
	 * 
	 * @param amount the minimum amount
	 * @return whether it can provide it or not
	 */
	default boolean check(int amount) {
		return amount <= this.amount();
	}


	/**
	 * Checks if this provider is empty.
	 * 
	 * @return if this provider is empty
	 */
	default boolean isEmpty() {
		return this.amount() <= 0;
	}


	/**
	 * Checks if this provider is full.
	 * 
	 * @return if this provider is full.
	 */
	default boolean isFull() {
		return this.space() <= 0;
	}


	/**
	 * Returns the type of this provider. Should return null if the type can be replaced.
	 * 
	 * @return the type
	 */
	T getType();

	/**
	 * Sets the type of this provider.
	 * 
	 * @param type the new type
	 */
	void setType(T type);

	/**
	 * Checks whether this provider can change it's type to the given type.
	 * 
	 * @param type the new type
	 * @return if this provider can change it's type
	 */
	boolean canChangeType(T type);

	/**
	 * Checks if the given type matches with the type of this provider.
	 * 
	 * @param type the type to be checked with
	 * @return if the types match
	 */
	boolean match(T type);

}
