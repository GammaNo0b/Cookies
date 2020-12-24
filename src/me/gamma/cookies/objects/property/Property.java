
package me.gamma.cookies.objects.property;


import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.Cookies;



public abstract class Property<T> {

	private String name;

	protected Property(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}


	public NamespacedKey getKey() {
		return new NamespacedKey(Cookies.getPlugin(Cookies.class), name);
	}


	public abstract void store(PersistentDataHolder holder, T value);


	public void storeEmpty(PersistentDataHolder holder) {
		this.store(holder, null);
	}
	
	
	public void remove(PersistentDataHolder holder) {
		holder.getPersistentDataContainer().remove(this.getKey());
	}


	public abstract T fetch(PersistentDataHolder holder);


	public void transfer(PersistentDataHolder from, PersistentDataHolder to) {
		this.store(to, this.fetch(from));
	}


	public abstract boolean isPropertyOf(PersistentDataHolder container);

}
