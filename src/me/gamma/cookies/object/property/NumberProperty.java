
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataHolder;



public abstract class NumberProperty<N extends Number> extends PrimitiveProperty<N> {

	protected final N min;
	protected final N max;

	public NumberProperty(String name, Class<N> clazz, N min, N max) {
		super(name, clazz);
		this.min = min;
		this.max = max;
	}


	/**
	 * Increases the given holder by the given amount and returns what could not be increased.
	 * 
	 * @param holder the holder
	 * @param amount the amount
	 * @return the amount it could not increase
	 */
	public abstract N increase(PersistentDataHolder holder, N amount);

	/**
	 * Adds the given amount to the given holder if possible and returns true, or false, if the holder is to full.
	 * 
	 * @param holder the holder
	 * @param amount the amount
	 * @return if the amount could be added
	 */
	public abstract boolean add(PersistentDataHolder holder, N amount);

	/**
	 * Decreases the given holder by the given amount and returns what could not be decreased.
	 * 
	 * @param holder the holder
	 * @param amount the amount
	 * @return the amount it could not decrease
	 */
	public abstract N decrease(PersistentDataHolder holder, N amount);

	/**
	 * Removes the given amount from the given holder if possible and returns true, or false, if the holder is to empty.
	 * 
	 * @param holder the holder
	 * @param amount the amount
	 * @return if the amount could be removed
	 */
	public abstract boolean remove(PersistentDataHolder holder, N amount);


	/**
	 * Returns the minimum value of this property.
	 * 
	 * @return the minimum
	 */
	public N getMin() {
		return this.min;
	}


	/**
	 * Returns the maximum value of this property.
	 * 
	 * @return the maximum value
	 */
	public N getMax() {
		return this.max;
	}

}
