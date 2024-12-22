
package me.gamma.cookies.object.property;

import org.bukkit.persistence.PersistentDataContainer;

public class ByteArrayProperty extends PrimitiveProperty<byte[]> {

	public ByteArrayProperty(String name) {
		super(name, byte[].class);
	}

	@Override
	public byte[] emptyValue(PersistentDataContainer container) {
		return new byte[0];
	}

}
