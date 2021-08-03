
package me.gamma.cookies.objects.property;


import java.util.UUID;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;



public class UUIDProperty extends Property<long[], UUID> {

	public UUIDProperty(String name) {
		super(name);
	}


	@Override
	public PersistentDataType<long[], UUID> getPersistentDataType() {
		return new PersistentDataType<long[], UUID>() {

			@Override
			public long[] toPrimitive(UUID uuid, PersistentDataAdapterContext context) {
				return new long[] {
					uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()
				};
			}


			@Override
			public Class<long[]> getPrimitiveType() {
				return long[].class;
			}


			@Override
			public Class<UUID> getComplexType() {
				return UUID.class;
			}


			@Override
			public UUID fromPrimitive(long[] value, PersistentDataAdapterContext context) {
				return new UUID(value[0], value[1]);
			}
		};
	}


	@Override
	public UUID emptyValue() {
		return new UUID(0, 0);
	}

}
