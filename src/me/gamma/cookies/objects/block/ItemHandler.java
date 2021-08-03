
package me.gamma.cookies.objects.block;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.ItemFilter;
import me.gamma.cookies.objects.recipe.CookieRecipe;
import me.gamma.cookies.util.Utilities;



public interface ItemHandler {

	public static boolean hasItem(List<ItemProvider> items, int slot) {
		return !Utilities.isEmpty(items.get(slot).get());
	}


	public static boolean hasItem(List<ItemProvider> items, int slot, ItemStack type, boolean inverse) {
		ItemStack stack = items.get(slot).get();
		return CookieRecipe.sameIngredient(stack, type) ^ inverse;
	}


	public static int getFirstFreeSlot(List<ItemProvider> items) {
		for(int i = 0; i < items.size(); i++)
			if(!hasItem(items, i))
				return i;
		return -1;
	}


	public static Set<Integer> getFreeSlots(List<ItemProvider> items) {
		Set<Integer> slots = new HashSet<>();
		for(int i = 0; i < items.size(); i++)
			if(!hasItem(items, i))
				slots.add(i);
		return slots;
	}


	public static int getFirstSlotWith(List<ItemProvider> items, ItemStack type, boolean inverse) {
		for(int i = 0; i < items.size(); i++)
			if(hasItem(items, i, type, inverse))
				return i;
		return -1;
	}


	public static Set<Integer> getSlotsWith(List<ItemProvider> items, ItemStack type, boolean inverse) {
		Set<Integer> slots = new HashSet<>();
		for(int i = 0; i < items.size(); i++)
			if(hasItem(items, i, type, inverse))
				slots.add(i);
		return slots;
	}


	public static ItemProvider getFirstOfType(List<ItemProvider> items, ItemStack type, boolean inverse) {
		int slot = getFirstSlotWith(items, type, inverse);
		if(slot == -1)
			return null;
		return items.get(slot);
	}


	public static List<ItemProvider> getOfType(List<ItemProvider> items, ItemStack type, boolean inverse) {
		return getSlotsWith(items, type, inverse).stream().map(items::get).collect(Collectors.toList());
	}


	public static List<ItemProvider> filter(List<ItemProvider> items, ItemFilter filter) {
		return items.stream().filter(provider -> filter.matches(provider.get())).collect(Collectors.toList());
	}


	public static ItemProvider filterFirst(List<ItemProvider> items, ItemFilter filter) {
		for(ItemProvider provider : items) {
			if(filter.matches(provider.get()))
				return provider;
		}
		return null;
	}

}
