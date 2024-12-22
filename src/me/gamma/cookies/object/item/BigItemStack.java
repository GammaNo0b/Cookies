
package me.gamma.cookies.object.item;


import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.ItemUtils;



public class BigItemStack {

	public static final BigItemStack EMPTY = new BigItemStack(Material.AIR, 0);

	private ItemStack stack;
	private int amount;

	public BigItemStack(ItemStack type, int amount) {
		if(type == null) {
			this.stack = null;
			this.amount = 0;
		} else {
			this.stack = type.clone();
			this.stack.setAmount(1);
			this.amount = amount;
		}
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
		return this.stack;
	}


	public void setStack(ItemStack stack) {
		this.stack = stack;
	}


	public boolean sameType(BigItemStack other) {
		return this.sameType(other.stack);
	}


	public boolean sameType(ItemStack stack) {
		return ItemUtils.similar(this.stack, stack);
	}


	public boolean isSimilar(BigItemStack other) {
		return this.isSimilar(other.stack);
	}


	public boolean isSimilar(ItemStack stack) {
		return ItemUtils.equals(this.stack, stack);
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
		return this.stack == null || this.stack.getType() == Material.AIR || this.amount == 0;
	}


	public int getAmount() {
		return this.amount;
	}


	@Override
	public String toString() {
		return this.stack == null ? "null" : this.amount + " X " + this.stack.toString();
	}


	public void drop(Location location) {
		if(this.isEmpty())
			return;

		int max = this.stack.getMaxStackSize();
		int full = this.amount / max;

		ItemStack stack = this.stack.clone();
		stack.setAmount(max);
		for(int i = 0; i < full; i++)
			ItemUtils.dropItem(stack, location);

		stack.setAmount(this.amount - full * max);
		ItemUtils.dropItem(stack, location);
	}

}
