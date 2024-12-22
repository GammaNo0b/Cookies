
package me.gamma.cookies.object.property;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.Cookies;



public class ListProperty<T, P extends AbstractProperty<T>> extends Property<PersistentDataContainer, List<T>> {

	private static final NamespacedKey size = new NamespacedKey(Cookies.INSTANCE, "size");

	private final Function<String, P> propertyGenerator;

	public ListProperty(String name, Function<String, P> propertyGenerator) {
		super(name);
		this.propertyGenerator = propertyGenerator;
	}


	@Override
	public PersistentDataType<PersistentDataContainer, List<T>> getPersistentDataType() {
		return new PersistentDataType<PersistentDataContainer, List<T>>() {

			@Override
			public Class<PersistentDataContainer> getPrimitiveType() {
				return PersistentDataContainer.class;
			}


			@SuppressWarnings("unchecked")
			@Override
			public Class<List<T>> getComplexType() {
				return (Class<List<T>>) new ArrayList<T>().getClass();
			}


			@Override
			public PersistentDataContainer toPrimitive(List<T> list, PersistentDataAdapterContext context) {
				PersistentDataContainer container = context.newPersistentDataContainer();
				int size = list.size();
				container.set(ListProperty.size, PersistentDataType.INTEGER, size);
				for(int i = 0; i < size; i++)
					ListProperty.this.propertyGenerator.apply(String.valueOf(i)).store(container, list.get(i));
				return container;
			}


			@Override
			public List<T> fromPrimitive(PersistentDataContainer container, PersistentDataAdapterContext context) {
				int size = container.get(ListProperty.size, PersistentDataType.INTEGER);
				List<T> list = new ArrayList<>(size);
				for(int i = 0; i < size; i++)
					list.add(ListProperty.this.propertyGenerator.apply(String.valueOf(i)).fetch(container));
				return list;
			}

		};
	}


	@Override
	public List<T> emptyValue(PersistentDataContainer container) {
		return new ArrayList<>();
	}

}
