
package me.gamma.cookies.objects.property;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.util.Utilities;



public class ItemStackProperty extends Property<String, ItemStack> {

	public ItemStackProperty(String name) {
		super(name);
	}


	@Override
	public PersistentDataType<String, ItemStack> getPersistentDataType() {
		return new PersistentDataType<String, ItemStack>() {

			@Override
			public String toPrimitive(ItemStack stack, PersistentDataAdapterContext context) {
				return Utilities.ItemStackToNBT(stack).asString();
			}


			@Override
			public Class<String> getPrimitiveType() {
				return String.class;
			}


			@Override
			public Class<ItemStack> getComplexType() {
				return ItemStack.class;
			}


			@Override
			public ItemStack fromPrimitive(String value, PersistentDataAdapterContext context) {
				return Utilities.NBTtoItemStack(Utilities.StringToNBT(value));
			}
		};
	}
	
	
	@Override
	public ItemStack emptyValue() {
		return new ItemStack(Material.AIR, 0);
	}

}
