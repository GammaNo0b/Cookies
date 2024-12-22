
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;



public class DoubleProperty extends NumberProperty<Double> {

	public DoubleProperty(String name) {
		this(name, Double.MAX_VALUE);
	}


	public DoubleProperty(String name, double max) {
		this(name, Double.MIN_VALUE, max);
	}


	public DoubleProperty(String name, double min, double max) {
		super(name, Double.class, min, max);
	}


	@Override
	public Double emptyValue(PersistentDataContainer container) {
		return 0.0D;
	}


	@Override
	public Double increase(PersistentDataHolder holder, Double amount) {
		double stored = this.fetch(holder);
		double add = Math.min(this.max - stored, amount);
		this.store(holder, stored + add);
		return amount - add;
	}


	@Override
	public boolean add(PersistentDataHolder holder, Double amount) {
		double stored = this.fetch(holder);
		if(this.max - stored < amount)
			return false;

		this.store(holder, stored + amount);
		return true;
	}


	@Override
	public Double decrease(PersistentDataHolder holder, Double amount) {
		double stored = this.fetch(holder);
		double remove = Math.min(stored - this.min, amount);
		this.store(holder, stored - remove);
		return amount - remove;
	}


	@Override
	public boolean remove(PersistentDataHolder holder, Double amount) {
		double stored = this.fetch(holder);
		if(stored - this.min < amount)
			return false;

		this.store(holder, stored - amount);
		return true;
	}

}
