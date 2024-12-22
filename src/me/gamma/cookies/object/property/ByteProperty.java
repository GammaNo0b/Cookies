
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;



public class ByteProperty extends NumberProperty<Byte> {

	public ByteProperty(String name) {
		this(name, Byte.MAX_VALUE);
	}


	public ByteProperty(String name, byte max) {
		this(name, Byte.MIN_VALUE, max);
	}


	public ByteProperty(String name, byte min, byte max) {
		super(name, Byte.class, min, max);
	}


	@Override
	public Byte emptyValue(PersistentDataContainer container) {
		return 0;
	}


	@Override
	public Byte increase(PersistentDataHolder holder, Byte amount) {
		byte stored = this.fetch(holder);
		byte add = (byte) Math.min(this.max - stored, amount);
		this.store(holder, (byte) (stored + add));
		return (byte) (amount - add);
	}


	@Override
	public boolean add(PersistentDataHolder holder, Byte amount) {
		byte stored = this.fetch(holder);
		if(this.max - stored < amount)
			return false;

		this.store(holder, (byte) (stored + amount));
		return true;
	}


	@Override
	public Byte decrease(PersistentDataHolder holder, Byte amount) {
		byte stored = this.fetch(holder);
		byte remove = (byte) Math.min(stored - this.min, amount);
		this.store(holder, (byte) (stored - remove));
		return (byte) (amount - remove);
	}


	@Override
	public boolean remove(PersistentDataHolder holder, Byte amount) {
		byte stored = this.fetch(holder);
		if(stored - this.min < amount)
			return false;

		this.store(holder, (byte) (stored - amount));
		return true;
	}

}
