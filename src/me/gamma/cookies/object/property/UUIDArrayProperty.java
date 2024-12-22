
package me.gamma.cookies.object.property;


import java.util.UUID;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;



public class UUIDArrayProperty extends Property<long[], UUID[]> {

	public UUIDArrayProperty(String name) {
		super(name);
	}


	@Override
	public PersistentDataType<long[], UUID[]> getPersistentDataType() {
		return new PersistentDataType<>() {

			@Override
			public Class<long[]> getPrimitiveType() {
				return long[].class;
			}


			@Override
			public Class<UUID[]> getComplexType() {
				return UUID[].class;
			}


			@Override
			public long[] toPrimitive(UUID[] complex, PersistentDataAdapterContext context) {
				long[] array = new long[complex.length * 2];
				int i = 0;
				for(UUID uuid : complex) {
					array[i++] = uuid.getMostSignificantBits();
					array[i++] = uuid.getLeastSignificantBits();
				}
				return array;
			}


			@Override
			public UUID[] fromPrimitive(long[] primitive, PersistentDataAdapterContext context) {
				UUID[] array = new UUID[primitive.length >> 1];
				for(int i = 0, j = 0; i < array.length; i++)
					array[i] = new UUID(primitive[j++], primitive[j++]);
				return array;
			}

		};
	}


	@Override
	public UUID[] emptyValue(PersistentDataContainer container) {
		return new UUID[0];
	}

}
