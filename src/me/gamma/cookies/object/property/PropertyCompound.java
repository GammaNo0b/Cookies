
package me.gamma.cookies.object.property;


import java.util.ArrayList;
import java.util.function.Function;

import org.bukkit.persistence.PersistentDataContainer;



public class PropertyCompound<C> extends AbstractProperty<C> {

	private final Function<Object[], C> constructor;
	private final int count;
	private final AbstractProperty<Object>[] properties;
	private final Function<C, Object>[] mapper;

	private PropertyCompound(Function<Object[], C> constructor, AbstractProperty<Object>[] properties, Function<C, Object>[] mapper) {
		this.count = properties.length;
		if(this.count != mapper.length)
			throw new IllegalArgumentException("The parameter arrays have to have the same length!");
		this.constructor = constructor;
		this.properties = properties;
		this.mapper = mapper;
	}


	@Override
	public C emptyValue(PersistentDataContainer container) {
		return null;
	}


	@Override
	public void store(PersistentDataContainer container, C value) {
		for(int i = 0; i < this.count; i++)
			this.properties[i].store(container, this.mapper[i].apply(value));
	}


	@Override
	public void storeEmpty(PersistentDataContainer container) {
		for(int i = 0; i < this.count; i++)
			this.properties[i].storeEmpty(container);
	}


	@Override
	public void remove(PersistentDataContainer container) {
		for(int i = 0; i < this.count; i++)
			this.properties[i].remove(container);
	}


	@Override
	public C fetch(PersistentDataContainer container) {
		Object[] args = new Object[this.count];
		for(int i = 0; i < this.count; i++)
			args[i] = this.properties[i].fetch(container);
		return this.constructor.apply(args);
	}


	@Override
	public C fetchEmpty(PersistentDataContainer container) {
		Object[] args = new Object[this.count];
		for(int i = 0; i < this.count; i++)
			args[i] = this.properties[i].fetchEmpty(container);
		return this.constructor.apply(args);
	}


	@Override
	public boolean isPropertyOf(PersistentDataContainer container) {
		for(AbstractProperty<?> property : this.properties)
			if(!property.isPropertyOf(container))
				return false;
		return true;
	}

	public static final class Builder<C> {

		private final Function<Object[], C> constructor;
		private final ArrayList<AbstractProperty<?>> properties = new ArrayList<>();
		private final ArrayList<Function<C, ?>> mapper = new ArrayList<>();

		public Builder(Function<Object[], C> constructor) {
			this.constructor = constructor;
		}


		public <D> Builder<C> addProperty(AbstractProperty<D> property, Function<C, D> mapper) {
			this.properties.add(property);
			this.mapper.add(mapper);
			return this;
		}


		@SuppressWarnings("unchecked")
		public PropertyCompound<C> build() {
			int size = this.properties.size();
			AbstractProperty<Object>[] properties = this.properties.toArray((AbstractProperty<Object>[]) new AbstractProperty<?>[size]);
			Function<C, Object>[] mapper = this.mapper.toArray((Function<C, Object>[]) new Function<?, ?>[size]);
			return new PropertyCompound<C>(this.constructor, properties, mapper);
		}

	}

}
