
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;



public class ShortProperty extends NumberProperty<Short> {

	public ShortProperty(String name) {
		this(name, Short.MAX_VALUE);
	}


	public ShortProperty(String name, short max) {
		this(name, Short.MIN_VALUE, max);
	}


	public ShortProperty(String name, short min, short max) {
		super(name, Short.class, min, max);
	}


	@Override
	public Short emptyValue(PersistentDataContainer container) {
		return 0;
	}


	@Override
	public Short increase(PersistentDataHolder holder, Short amount) {
		short stored = this.fetch(holder);
		short add = (short) Math.min(this.max - stored, amount);
		this.store(holder, (short) (stored + add));
		return (short) (amount - add);
	}


	@Override
	public boolean add(PersistentDataHolder holder, Short amount) {
		short stored = this.fetch(holder);
		if(this.max - stored < amount)
			return false;

		this.store(holder, (short) (stored + amount));
		return true;
	}


	@Override
	public Short decrease(PersistentDataHolder holder, Short amount) {
		short stored = this.fetch(holder);
		short remove = (short) Math.min(stored - this.min, amount);
		this.store(holder, (short) (stored - remove));
		return (short) (amount - remove);
	}


	@Override
	public boolean remove(PersistentDataHolder holder, Short amount) {
		short stored = this.fetch(holder);
		if(stored - this.min < amount)
			return false;

		this.store(holder, (short) (stored - amount));
		return true;
	}

}
