
package me.gamma.cookies.objects.property;


import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;



public class IntegerProperty extends Property<Integer> {

	protected IntegerProperty(String name) {
		super(name);
	}


	@Override
	public void store(PersistentDataHolder holder, Integer value) {
		if(value == null)
			value = 0;
		holder.getPersistentDataContainer().set(this.getKey(), PersistentDataType.INTEGER, value);
	}


	@Override
	public Integer fetch(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().get(this.getKey(), PersistentDataType.INTEGER);
	}


	public void increase(PersistentDataHolder holder, int amount) {
		this.store(holder, amount + this.fetch(holder));
	}


	@Override
	public boolean isPropertyOf(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().has(this.getKey(), PersistentDataType.INTEGER);
	}


	public static IntegerProperty create(String name) {
		return new IntegerProperty(name);
	}

}
