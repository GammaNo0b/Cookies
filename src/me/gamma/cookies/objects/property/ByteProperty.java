
package me.gamma.cookies.objects.property;


import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;



public class ByteProperty extends Property<Byte> {

	private ByteProperty(String name) {
		super(name);
	}


	@Override
	public void store(PersistentDataHolder holder, Byte value) {
		if(value == null)
			value = 0;
		holder.getPersistentDataContainer().set(this.getKey(), PersistentDataType.BYTE, value);
	}


	@Override
	public Byte fetch(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().get(this.getKey(), PersistentDataType.BYTE);
	}


	@Override
	public boolean isPropertyOf(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().has(this.getKey(), PersistentDataType.BYTE);
	}


	public static ByteProperty create(String name) {
		return new ByteProperty(name);
	}

}
