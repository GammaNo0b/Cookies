
package me.gamma.cookies.objects.property;


import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.util.BigItemStack;
import me.gamma.cookies.util.Utilities;



public class BigItemStackProperty extends Property<BigItemStack> {

	protected BigItemStackProperty(String name) {
		super(name);
	}


	@Override
	public void store(PersistentDataHolder holder, BigItemStack value) {
		if(value == null)
			value = BigItemStack.EMPTY;
		holder.getPersistentDataContainer().set(this.getKey(), PersistentDataType.STRING, Utilities.BigItemStackToNBT(value).toString());
	}


	@Override
	public BigItemStack fetch(PersistentDataHolder holder) {
		return Utilities.NBTtoBigItemStack(Utilities.StringToNBT(holder.getPersistentDataContainer().get(this.getKey(), PersistentDataType.STRING)));
	}


	@Override
	public boolean isPropertyOf(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().has(this.getKey(), PersistentDataType.STRING);
	}


	public static BigItemStackProperty create(String name) {
		return new BigItemStackProperty(name);
	}

}
