
package me.gamma.cookies.objects;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.recipe.CookieRecipe;
import me.gamma.cookies.util.BigItemStack;



public class ItemFilter {

	private final List<ItemStack> types;
	private final boolean whitelist;
	private final boolean ignoreNBT;

	public ItemFilter(boolean whitelist, boolean ignoreNBT) {
		this(new ArrayList<>(), whitelist, ignoreNBT);
	}


	public ItemFilter(List<ItemStack> types, boolean whitelist, boolean ignoreNBT) {
		this.whitelist = whitelist;
		this.types = types;
		this.ignoreNBT = ignoreNBT;
	}


	public void addFilterItem(ItemStack type) {
		this.types.add(type);
	}


	public boolean isWhitelist() {
		return whitelist;
	}


	public boolean isIgnoreNBT() {
		return ignoreNBT;
	}


	/**
	 * Returns the stack stored in this filter object that the given stack matches.
	 * 
	 * @param stack stack to be checked
	 * @return matching stack
	 */
	public ItemStack getMatchingStack(ItemStack stack) {
		if(stack == null || stack.getType() == Material.AIR || stack.getAmount() == 0)
			return null;
		for(ItemStack type : types)
			if(whitelist == (ignoreNBT ? CookieRecipe.sameType(stack, type) : CookieRecipe.sameIngredient(stack, type)))
				return type;
		return null;
	}


	/**
	 * Returns true if the give stack matches this filter.
	 * 
	 * @param stack ItemStack to be checked
	 * @return if the stack matches
	 */
	public boolean matches(ItemStack stack) {
		if(stack == null || stack.getType() == Material.AIR || stack.getAmount() == 0)
			return false;
		boolean result = false;
		for(ItemStack type : types) {
			if(ignoreNBT ? CookieRecipe.sameType(stack, type) : CookieRecipe.sameIngredient(stack, type)) {
				result = true;
				break;
			}
		}
		return result == whitelist;
	}
	
	public boolean matches(BigItemStack stack) {
		return this.matches(stack.getStack());
	}


	@Override
	public String toString() {
		return String.format("Whitelist: %b, IgnoreNBT: %b, Types: %s", this.whitelist, this.ignoreNBT, Arrays.toString(this.types.toArray(new ItemStack[this.types.size()])));
	}

}
