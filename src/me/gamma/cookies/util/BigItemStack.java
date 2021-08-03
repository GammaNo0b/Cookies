
package me.gamma.cookies.util;


import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.recipe.CookieRecipe;



public class BigItemStack {

	public static final BigItemStack EMPTY = new BigItemStack(Material.AIR, 0);

	private ItemStack stack;
	private int amount;

	public BigItemStack(ItemStack type, int amount) {
		this.stack = type.clone();
		this.stack.setAmount(1);
		this.amount = amount;
	}


	public BigItemStack(ItemStack type) {
		this(type, 0);
	}


	public BigItemStack(Material type, int amount) {
		this(new ItemStack(type), amount);
	}


	public BigItemStack(Material type) {
		this(new ItemStack(type), 0);
	}


	public void set(int amount) {
		this.amount = amount;
	}


	public void grow(int amount) {
		this.amount += amount;
	}


	public void shrink(int amount) {
		this.grow(-amount);
	}


	public ItemStack getStack() {
		return stack;
	}
	
	
	public boolean sameType(BigItemStack other) {
		return this.sameType(other.stack);
	}
	
	
	public boolean sameType(ItemStack stack) {
		return CookieRecipe.sameType(this.stack, stack);
	}


	public boolean isSimilar(BigItemStack other) {
		return this.isSimilar(other.stack);
	}


	public boolean isSimilar(ItemStack stack) {
		return CookieRecipe.sameIngredient(this.stack, stack);
	}


	public boolean isRepresentedIn(Collection<? extends ItemStack> stacks) {
		for(ItemStack stack : stacks)
			if(this.isSimilar(stack))
				return true;
		return false;
	}
	
	
	public boolean isTypeRepresentedIn(Collection<? extends ItemStack> stacks) {
		for(ItemStack stack : stacks)
			if(this.sameType(stack))
				return true;
		return false;
	}


	public boolean isEmpty() {
		return stack == null || stack.getType() == Material.AIR || amount == 0;
	}


	public int getAmount() {
		return amount;
	}


	@Override
	public String toString() {
		return this.stack == null ? "null" : this.amount + " X " + this.stack.toString();
	}

}
