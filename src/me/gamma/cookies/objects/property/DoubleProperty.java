package me.gamma.cookies.objects.property;

import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;


public class DoubleProperty extends Property<Double> {

	private DoubleProperty(String name) {
		super(name);
	}


	@Override
	public void store(PersistentDataHolder holder, Double value) {
		if(value == null)
			value = 0.0D;
		holder.getPersistentDataContainer().set(this.getKey(), PersistentDataType.DOUBLE, value);
	}


	@Override
	public Double fetch(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().get(this.getKey(), PersistentDataType.DOUBLE);
	}


	@Override
	public boolean isPropertyOf(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().has(this.getKey(), PersistentDataType.DOUBLE);
	}
	
	public static DoubleProperty create(String name) {
		return new DoubleProperty(name);
	}

}
