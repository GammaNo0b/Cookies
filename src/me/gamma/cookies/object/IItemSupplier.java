
package me.gamma.cookies.object;


import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;



public interface IItemSupplier extends Supplier<ItemStack> {

	/**
	 * Creates a new item stack with initial properties stored inside the given data holder.
	 * 
	 * @param holder the data holder
	 * @return the created item stack
	 */
	default ItemStack get(PersistentDataHolder holder) {
		return this.get();
	}


	/**
	 * Returns a new {@link IItemSupplier} of the given material.
	 * 
	 * @param material the material
	 * @return the item supplier
	 */
	static IItemSupplier of(Material material) {
		return () -> new ItemStack(material);
	}


	/**
	 * Returns a new {@link IItemSupplier} of the given item stack.
	 * 
	 * @param stack the item stack
	 * @return the item supplier
	 */
	static IItemSupplier of(ItemStack stack) {
		return () -> stack;
	}

}
