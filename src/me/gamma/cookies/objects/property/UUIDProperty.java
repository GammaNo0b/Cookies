
package me.gamma.cookies.objects.property;


import java.util.UUID;

import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;



public class UUIDProperty extends Property<UUID> {

	private UUIDProperty(String name) {
		super(name);
	}


	@Override
	public void store(PersistentDataHolder holder, UUID value) {
		long[] bits = new long[] { value.getMostSignificantBits(), value.getLeastSignificantBits() };
		holder.getPersistentDataContainer().set(this.getKey(), PersistentDataType.LONG_ARRAY, bits);
	}
	
	@Override
	public void storeEmpty(PersistentDataHolder holder) {
		this.store(holder, UUID.fromString("000000000000-0000-0000-0000-00000000"));
	}


	@Override
	public UUID fetch(PersistentDataHolder holder) {
		long[] bits = holder.getPersistentDataContainer().get(this.getKey(), PersistentDataType.LONG_ARRAY);
		return new UUID(bits[0], bits[1]);
	}


	@Override
	public boolean isPropertyOf(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().has(this.getKey(), PersistentDataType.LONG_ARRAY);
	}


	public static UUIDProperty create(String name) {
		return new UUIDProperty(name);
	}

}
