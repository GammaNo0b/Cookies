
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.util.math.MathHelper;



public class IntegerProperty extends NumberProperty<Integer> {

	public IntegerProperty(String name) {
		this(name, Integer.MAX_VALUE);
	}


	public IntegerProperty(String name, int max) {
		this(name, Integer.MIN_VALUE, max);
	}


	public IntegerProperty(String name, int min, int max) {
		super(name, Integer.class, min, max);
	}


	@Override
	public Integer emptyValue(PersistentDataContainer container) {
		return 0;
	}


	@Override
	public Integer increase(PersistentDataHolder holder, Integer amount) {
		int stored = this.fetch(holder);
		int add = Math.min(this.max - stored, amount);
		this.store(holder, stored + add);
		return amount - add;
	}


	@Override
	public boolean add(PersistentDataHolder holder, Integer amount) {
		int stored = this.fetch(holder);
		if(this.max - stored < amount)
			return false;

		this.store(holder, stored + amount);
		return true;
	}


	@Override
	public Integer decrease(PersistentDataHolder holder, Integer amount) {
		int stored = this.fetch(holder);
		int remove = Math.min(stored - this.min, amount);
		if(remove > 0)
			this.store(holder, stored - remove);
		return amount - remove;
	}


	@Override
	public boolean remove(PersistentDataHolder holder, Integer amount) {
		int stored = this.fetch(holder);
		if(stored - this.min < amount)
			return false;

		this.store(holder, stored - amount);
		return true;
	}


	public int cycle(PersistentDataHolder holder, int amount, int mod) {
		int stored = this.fetch(holder);
		this.store(holder, MathHelper.mod(stored += amount, mod));
		return stored;
	}

}
