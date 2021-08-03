
package me.gamma.cookies.objects.property;


import java.util.Objects;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.Cookies;



public abstract class Property<P, C> {

	private String name;

	public Property(String name) {
		this.name = name;
	}


	public String getName() {
		return this.name;
	}


	public NamespacedKey getKey() {
		return new NamespacedKey(Cookies.INSTANCE, name);
	}


	public abstract PersistentDataType<P, C> getPersistentDataType();

	public abstract C emptyValue();


	public final void store(PersistentDataHolder holder, C value) {
		if(value != null)
			holder.getPersistentDataContainer().set(this.getKey(), this.getPersistentDataType(), value);
		else
			this.storeEmpty(holder);
	}


	public final void storeEmpty(PersistentDataHolder holder) {
		this.store(holder, this.emptyValue());
	}


	public final void remove(PersistentDataHolder holder) {
		holder.getPersistentDataContainer().remove(this.getKey());
	}


	public final C fetch(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().get(this.getKey(), this.getPersistentDataType());
	}


	public final void transfer(PersistentDataHolder from, PersistentDataHolder to) {
		this.store(to, this.fetch(from));
	}


	public final boolean isSame(PersistentDataHolder holder1, PersistentDataHolder holder2) {
		return Objects.equals(this.fetch(holder1), this.fetch(holder2));
	}


	public final boolean isPropertyOf(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().has(this.getKey(), this.getPersistentDataType());
	}

}
