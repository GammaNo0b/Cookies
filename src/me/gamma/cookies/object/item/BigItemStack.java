
package me.gamma.cookies.object.item;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.ItemUtils;



public class BigItemStack {

	public static final BigItemStack EMPTY = new BigItemStack(Material.AIR, 0, 0);

	private ItemStack stack;
	private int amount;
	private int maxStackSize;
	private boolean locked = false;

	public BigItemStack(ItemStack type, int amount, int maxStackSize) {
		this.setType(type);
		this.amount = amount;
		this.maxStackSize = maxStackSize;
	}


	public BigItemStack(Material type, int amount, int maxStackSize) {
		this(new ItemStack(type), amount, maxStackSize);
	}


	public ItemStack getType() {
		return this.stack;
	}


	public void setType(ItemStack stack) {
		if(stack == null)
			return;

		this.stack = stack.clone();
		this.stack.setAmount(1);
	}


	public int getAmount() {
		return this.amount;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}


	public void grow(int amount) {
		this.amount += amount;
	}


	public void shrink(int amount) {
		this.amount -= amount;
	}


	public int getMaxStackSize() {
		return this.maxStackSize;
	}


	public void setMaxStackSize(int maxStackSize) {
		this.maxStackSize = maxStackSize;
	}


	public boolean isLocked() {
		return this.locked;
	}


	public void setLocked(boolean locked) {
		this.locked = locked;
	}


	public boolean toggleLocked() {
		return (this.locked = !this.locked);
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


	public boolean isEmpty() {
		return this.stack == null || this.stack.getType() == Material.AIR || this.amount == 0;
	}


	@Override
	public String toString() {
		return this.stack == null ? "null" : this.amount + " x " + this.stack.toString();
	}


	public void drop(Location location) {
		if(this.isEmpty())
			return;

		int max = this.stack.getMaxStackSize();
		int amount = this.amount;
		while(amount > 0) {
			int drop = Math.min(amount, max);
			amount -= drop;
			ItemStack stack = this.stack.clone();
			stack.setAmount(drop);
			ItemUtils.dropItem(stack, location);
		}
	}

}
