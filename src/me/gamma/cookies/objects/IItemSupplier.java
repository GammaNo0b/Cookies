
package me.gamma.cookies.objects;


import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;



public interface IItemSupplier extends Supplier<ItemStack> {

	default boolean shouldRegister() {
		return true;
	}


	static IItemSupplier of(Material material) {
		return of(new ItemStack(material));
	}


	static IItemSupplier of(ItemStack stack) {
		return () -> stack;
	}

}
