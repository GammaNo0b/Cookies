
package me.gamma.cookies.objects.property;


import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;



public class BooleanProperty extends Property<Boolean> {

	private BooleanProperty(String name) {
		super(name);
	}


	@Override
	public void store(PersistentDataHolder holder, Boolean value) {
		holder.getPersistentDataContainer().set(this.getKey(), PersistentDataType.BYTE, (byte) (value == null ? -1 : !value ? 0 : 1));
	}


	@Override
	public Boolean fetch(PersistentDataHolder holder) {
		byte b = holder.getPersistentDataContainer().get(this.getKey(), PersistentDataType.BYTE);
		if(b < 0)
			return null;
		else
			return b == 0 ? false : true;
	}
	
	
	public boolean toggle(PersistentDataHolder holder) {
		boolean b = !this.fetch(holder);
		this.store(holder, b);
		return b;
	}


	@Override
	public boolean isPropertyOf(PersistentDataHolder container) {
		return container.getPersistentDataContainer().has(this.getKey(), PersistentDataType.BYTE);
	}


	public static BooleanProperty create(String name) {
		return new BooleanProperty(name);
	}

}
