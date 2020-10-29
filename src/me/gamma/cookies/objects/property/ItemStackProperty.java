
package me.gamma.cookies.objects.property;


import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.util.Utilities;



public class ItemStackProperty extends Property<ItemStack> {

	protected ItemStackProperty(String name) {
		super(name);
	}


	@Override
	public void store(PersistentDataHolder holder, ItemStack value) {
		holder.getPersistentDataContainer().set(this.getKey(), PersistentDataType.STRING, Utilities.ItemStackToNBT(value).asString());
	}


	@Override
	public ItemStack fetch(PersistentDataHolder holder) {
		return Utilities.NBTtoItemStack(Utilities.StringToNBT(holder.getPersistentDataContainer().get(this.getKey(), PersistentDataType.STRING)));
	}


	@Override
	public boolean isPropertyOf(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().has(this.getKey(), PersistentDataType.STRING);
	}


	public static ItemStackProperty create(String name) {
		return new ItemStackProperty(name);
	}

}
