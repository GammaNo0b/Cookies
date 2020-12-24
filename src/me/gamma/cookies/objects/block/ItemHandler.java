
package me.gamma.cookies.objects.block;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.IItemSupplier;
import me.gamma.cookies.objects.recipe.CookieRecipe;



public interface ItemHandler {

	static boolean hasItem(List<IItemSupplier> items, int slot) {
		ItemStack stack = items.get(slot).get();
		return stack != null && stack.getType() != Material.AIR && stack.getAmount() > 0;
	}


	static boolean hasItem(List<IItemSupplier> items, int slot, ItemStack type) {
		ItemStack stack = items.get(slot).get();
		return stack == null || CookieRecipe.sameIngredient(stack, type);
	}


	static int getFirstFreeSlot(List<IItemSupplier> items) {
		for(int i = 0; i < items.size(); i++)
			if(!hasItem(items, i))
				return i;
		return -1;
	}


	static Set<Integer> getFreeSlots(List<IItemSupplier> items) {
		Set<Integer> slots = new HashSet<>();
		for(int i = 0; i < items.size(); i++)
			if(!hasItem(items, i))
				slots.add(i);
		return slots;
	}


	static int getFirstSlotWith(List<IItemSupplier> items, ItemStack type) {
		for(int i = 0; i < items.size(); i++)
			if(hasItem(items, i, type))
				return i;
		return -1;
	}


	static Set<Integer> getSlotsWith(List<IItemSupplier> items, ItemStack type) {
		Set<Integer> slots = new HashSet<>();
		for(int i = 0; i < items.size(); i++)
			if(hasItem(items, i, type))
				slots.add(i);
		return slots;
	}

}
