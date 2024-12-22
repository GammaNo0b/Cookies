package me.gamma.cookies.object.property;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;

public class LongProperty extends NumberProperty<Long> {

	public LongProperty(String name) {
		this(name, Long.MAX_VALUE);
	}

	public LongProperty(String name, long max) {
		this(name, Long.MIN_VALUE, max);
	}

	public LongProperty(String name, long min, long max) {
		super(name, Long.class, min, max);
	}

	@Override
	public Long emptyValue(PersistentDataContainer container) {
		return 0L;
	}


	@Override
	public Long increase(PersistentDataHolder holder, Long amount) {
		long stored = this.fetch(holder);
		long add = Math.min(this.max - stored, amount);
		this.store(holder, stored + add);
		return amount - add;
	}


	@Override
	public boolean add(PersistentDataHolder holder, Long amount) {
		long stored = this.fetch(holder);
		if(this.max - stored < amount)
			return false;

		this.store(holder, stored + amount);
		return true;
	}


	@Override
	public Long decrease(PersistentDataHolder holder, Long amount) {
		long stored = this.fetch(holder);
		long remove = Math.min(stored - this.min, amount);
		this.store(holder, stored - remove);
		return amount - remove;
	}


	@Override
	public boolean remove(PersistentDataHolder holder, Long amount) {
		long stored = this.fetch(holder);
		if(stored - this.min < amount)
			return false;

		this.store(holder, stored - amount);
		return true;
	}

}
