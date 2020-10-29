package me.gamma.cookies.objects.property;

import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;


public class StringProperty extends Property<String> {
	
	private StringProperty(String name) {
		super(name);
	}

	@Override
	public void store(PersistentDataHolder holder, String value) {
		holder.getPersistentDataContainer().set(this.getKey(), PersistentDataType.STRING, value);
	}
	
	@Override
	public void storeEmpty(PersistentDataHolder holder) {
		this.store(holder, "");
	}
	
	@Override
	public String fetch(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().get(this.getKey(), PersistentDataType.STRING);
	}
	
	@Override
	public boolean isPropertyOf(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().has(this.getKey(), PersistentDataType.STRING);
	}
	
	public static StringProperty create(String name) {
		return new StringProperty(name);
	}

}
