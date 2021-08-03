
package me.gamma.cookies.objects.block;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.IItemSupplier;
import me.gamma.cookies.util.BigItemStack;
import me.gamma.cookies.util.Provider;



public interface ItemProvider extends IItemSupplier, Provider<ItemStack> {

	public static ItemProvider fromBigItemStack(BigItemStack stack) {
		return new ItemProvider() {

			@Override
			public void set(ItemStack value) {
				if(!stack.isSimilar(stack))
					return;
				stack.grow(value.getAmount());
			}


			@Override
			public ItemStack get() {
				int amount = stack.getStack().getMaxStackSize();
				if(stack.getAmount() < amount)
					amount = stack.getAmount();
				stack.shrink(amount);
				ItemStack result = new ItemStack(stack.getStack());
				result.setAmount(amount);
				return result;
			}

		};
	}


	public static ItemProvider fromInventory(Inventory inventory, int slot) {
		return new ItemProvider() {

			@Override
			public ItemStack get() {
				return inventory.getItem(slot);
			}


			@Override
			public void set(ItemStack stack) {
				inventory.setItem(slot, stack);
			}

		};
	}


	public static List<ItemProvider> fromInventory(Inventory inventory, int... slots) {
		ArrayList<ItemProvider> providers = new ArrayList<>();
		for(int slot : slots) {
			providers.add(fromInventory(inventory, slot));
		}
		return providers;
	}


	public static List<ItemProvider> createItemProviders(Inventory inventory) {
		List<ItemProvider> providers = new ArrayList<>(inventory.getSize());
		for(int i = 0; i < inventory.getSize(); i++)
			providers.add(fromInventory(inventory, i));
		return providers;
	}

}
