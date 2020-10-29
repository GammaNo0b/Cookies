
package me.gamma.cookies.util;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;



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


	public void add(int amount) {
		this.amount += amount;
	}


	public ItemStack getStack() {
		return stack;
	}
	
	
	public boolean isSimilar(BigItemStack other) {
		return this.stack.isSimilar(other.stack);
	}
	
	
	public boolean isSimilar(ItemStack stack) {
		return this.stack.isSimilar(stack);
	}
	
	
	public boolean isEmpty() {
		return stack == null || stack.getType() == Material.AIR || amount == 0;
	}


	public int getAmount() {
		return amount;
	}

}
