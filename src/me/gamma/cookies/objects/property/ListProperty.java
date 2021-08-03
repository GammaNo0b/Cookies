
package me.gamma.cookies.objects.property;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;



public abstract class ListProperty<T> extends Property<String, List<T>> {

	public ListProperty(String name) {
		super(name);
	}


	@Override
	public PersistentDataType<String, List<T>> getPersistentDataType() {
		return new PersistentDataType<String, List<T>>() {

			@Override
			public Class<String> getPrimitiveType() {
				return String.class;
			}


			@SuppressWarnings("unchecked")
			@Override
			public Class<List<T>> getComplexType() {
				return (Class<List<T>>) new ArrayList<T>(0).getClass();
			}


			@Override
			public String toPrimitive(List<T> list, PersistentDataAdapterContext context) {
				if(list == null || list.size() == 0) {
					return "";
				}
				StringBuilder builder = new StringBuilder(ListProperty.this.toString(list.get(0)));
				for(int i = 1; i < list.size(); i++)
					builder.append(ListProperty.this.getDividingRegex() + list.get(i));
				return builder.toString();
			}


			@Override
			public List<T> fromPrimitive(String string, PersistentDataAdapterContext context) {
				List<T> list = new ArrayList<>();
				for(String str : string.split(ListProperty.this.getDividingRegex()))
					list.add(fromString(str));
				return list;
			}

		};
	}


	@Override
	public List<T> emptyValue() {
		return new ArrayList<>();
	}


	public abstract String getDividingRegex();


	public abstract T fromString(String string);


	public abstract String toString(T value);

}
