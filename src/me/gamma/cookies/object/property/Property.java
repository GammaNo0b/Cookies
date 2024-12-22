
package me.gamma.cookies.object.property;


import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.Cookies;



public abstract class Property<P, C> extends AbstractProperty<C> {

	/**
	 * The key of this property that will be used to store the data.
	 */
	protected final NamespacedKey key;

	/**
	 * Creates a new property instance.
	 * 
	 * @param name the name
	 */
	public Property(String name) {
		this.key = new NamespacedKey(Cookies.INSTANCE, name);
	}


	/**
	 * Returns the name of this property.
	 * 
	 * @return the name
	 */
	public final String getName() {
		return this.key.getKey();
	}


	/**
	 * Returns the {@link PersistentDataType} to convert the data between it's primitive and complex form.
	 * 
	 * @return the {@link PersistentDataType}
	 */
	public abstract PersistentDataType<P, C> getPersistentDataType();


	@Override
	public void store(PersistentDataContainer container, C value) {
		if(container != null)
			container.set(this.key, this.getPersistentDataType(), value == null ? this.emptyValue(container) : value);
	}


	@Override
	public void storeEmpty(PersistentDataContainer container) {
		if(container != null)
			this.store(container, null);
	}


	@Override
	public final void remove(PersistentDataContainer container) {
		if(container != null)
			container.remove(this.key);
	}


	@Override
	public final C fetch(PersistentDataContainer container) {
		return container == null ? null : container.get(this.key, this.getPersistentDataType());
	}


	@Override
	public final C fetchEmpty(PersistentDataContainer container) {
		C value = this.fetch(container);
		return value == null ? this.emptyValue(container) : value;
	}


	@Override
	public final boolean isPropertyOf(PersistentDataContainer container) {
		return container != null && container.has(this.key, this.getPersistentDataType());
	}

}
