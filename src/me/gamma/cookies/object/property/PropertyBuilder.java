
package me.gamma.cookies.object.property;


import java.util.HashSet;
import java.util.function.Function;

import org.bukkit.persistence.PersistentDataHolder;



public class PropertyBuilder {

	private final HashSet<Entry<?>> properties = new HashSet<>();

	public PropertyBuilder() {}


	public <T> PropertyBuilder add(AbstractProperty<T> property) {
		this.properties.add(new Entry<>(property));
		return this;
	}


	public <T> PropertyBuilder add(AbstractProperty<T> property, T defaultValue) {
		this.properties.add(new Entry<>(property, defaultValue));
		return this;
	}


	public PropertyBuilder add(Function<Integer, AbstractProperty<?>> generator, int n) {
		for(int i = 0; i < n; i++)
			this.properties.add(new Entry<>(generator.apply(i)));
		return this;
	}


	public <T, P extends AbstractProperty<T>> PropertyBuilder addAll(P[] properties) {
		for(AbstractProperty<?> property : properties)
			this.add(property);
		return this;
	}


	public <T, P extends AbstractProperty<T>> PropertyBuilder addAll(P[] properties, T defaultValue) {
		for(AbstractProperty<T> property : properties)
			this.add(property, defaultValue);
		return this;
	}


	public PropertyBuilder addAll(PropertyBuilder builder) {
		if(builder != this)
			this.properties.addAll(builder.properties);

		return this;
	}


	public HashSet<Entry<?>> build() {
		return this.properties;
	}


	public void buildAndStore(PersistentDataHolder holder) {
		this.properties.forEach(e -> e.store(holder));
	}


	public void buildAndTransfer(PersistentDataHolder origin, PersistentDataHolder destination) {
		if(origin != null && destination != null)
			this.properties.forEach(e -> e.transfer(origin, destination));
	}

	public static class Entry<T> {

		private final AbstractProperty<T> property;
		private final T defaultValue;

		Entry(AbstractProperty<T> property) {
			this(property, null);
		}


		Entry(AbstractProperty<T> property, T defaultValue) {
			this.property = property;
			this.defaultValue = defaultValue;
		}


		public AbstractProperty<T> getProperty() {
			return this.property;
		}


		public T getDefaultValue() {
			return this.defaultValue;
		}


		private void store(PersistentDataHolder holder) {
			if(this.defaultValue == null) {
				this.property.storeEmpty(holder);
			} else {
				this.property.store(holder, this.defaultValue);
			}
		}


		private void transfer(PersistentDataHolder origin, PersistentDataHolder destination) {
			T value = this.property.fetch(origin);
			this.property.store(destination, value == null ? this.defaultValue : value);
		}

	}

}
