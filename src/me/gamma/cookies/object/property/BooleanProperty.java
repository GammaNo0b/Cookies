
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;



public class BooleanProperty extends Property<Byte, Boolean> {

	public BooleanProperty(String name) {
		super(name);
	}


	@Override
	public PersistentDataType<Byte, Boolean> getPersistentDataType() {
		return new PersistentDataType<Byte, Boolean>() {

			@Override
			public Byte toPrimitive(Boolean bool, PersistentDataAdapterContext context) {
				return (byte) (bool ? 1 : 0);
			}


			@Override
			public Class<Byte> getPrimitiveType() {
				return Byte.class;
			}


			@Override
			public Class<Boolean> getComplexType() {
				return Boolean.class;
			}


			@Override
			public Boolean fromPrimitive(Byte value, PersistentDataAdapterContext context) {
				return value != 0;
			}

		};
	}


	@Override
	public Boolean emptyValue(PersistentDataContainer container) {
		return false;
	}


	public boolean toggle(PersistentDataHolder holder) {
		boolean b = !this.fetch(holder);
		this.store(holder, b);
		return b;
	}

}
