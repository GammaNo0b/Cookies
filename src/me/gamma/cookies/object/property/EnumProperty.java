
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.util.EnumUtils;



public class EnumProperty<E extends Enum<E>> extends Property<Integer, E> {

	private final Class<E> clazz;
	private final E[] values;

	public EnumProperty(String name, Class<E> clazz) {
		super(name);
		this.clazz = clazz;
		this.values = clazz.getEnumConstants();
		if(this.values == null)
			throw new IllegalArgumentException("The Class has to be an Enum!");
		if(this.values.length == 0)
			throw new IllegalArgumentException("The Enum Class has to provide at least one Enum Constant!");
	}


	@Override
	public PersistentDataType<Integer, E> getPersistentDataType() {
		return new PersistentDataType<Integer, E>() {

			@Override
			public E fromPrimitive(Integer value, PersistentDataAdapterContext context) {
				return EnumProperty.this.values[value];
			}


			@Override
			public Integer toPrimitive(E element, PersistentDataAdapterContext context) {
				return element.ordinal();
			}


			@Override
			public Class<Integer> getPrimitiveType() {
				return Integer.class;
			}


			@Override
			public Class<E> getComplexType() {
				return EnumProperty.this.clazz;
			}

		};
	}


	@Override
	public E emptyValue(PersistentDataContainer container) {
		return this.values[0];
	}


	public E cycle(PersistentDataHolder holder) {
		return this.cycle(holder, 1);
	}


	public E cycle(PersistentDataHolder holder, int amount) {
		E element = EnumUtils.cycle(this.fetch(holder), amount);
		this.store(holder, element);
		return element;
	}

}
