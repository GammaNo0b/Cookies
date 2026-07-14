
package me.gamma.cookies.object;


import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.collection.PersistentDataObject;



public interface IItemSupplier extends Supplier<ItemStack> {

	@Override
	default ItemStack get() {
		return this.get(_ -> {});
	}


	/**
	 * Creates a new item stack with initial properties stored inside the given data.
	 * 
	 * @param dataConsumer the data
	 * @return the created item stack
	 */
	ItemStack get(Consumer<PersistentDataObject> dataConsumer);


	/**
	 * Returns a new {@link IItemSupplier} of the given material.
	 * 
	 * @param material the material
	 * @return the item supplier
	 */
	static IItemSupplier of(final Material material) {
		return _ -> new ItemStack(material);
	}


	/**
	 * Returns a new {@link IItemSupplier} of the given item stack.
	 * 
	 * @param stack the item stack
	 * @return the item supplier
	 */
	static IItemSupplier of(final ItemStack stack) {
		return _ -> stack.clone();
	}

}
