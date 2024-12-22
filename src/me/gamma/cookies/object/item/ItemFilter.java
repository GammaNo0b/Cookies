
package me.gamma.cookies.object.item;


import java.util.Arrays;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.item.tools.ItemFilterItem;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public class ItemFilter implements Filter<ItemStack> {

	public static final int SIZE = 16;

	private final ItemStack[] types;
	private CountComparison countComparator;
	private boolean whitelisted;
	private boolean ignoreNBT;
	
	public ItemFilter() {
		this(CountComparison.IGNORE, true, true);
	}

	public ItemFilter(CountComparison countComparator, boolean whitelisted, boolean ignoreNBT) {
		this.types = new ItemStack[SIZE];
		this.countComparator = countComparator;
		this.whitelisted = whitelisted;
		this.ignoreNBT = ignoreNBT;
	}


	public void setFilterItem(int slot, ItemStack type) {
		this.types[slot] = type;
	}


	public ItemStack getFilterItem(int slot) {
		return this.types[slot];
	}


	public ItemStack[] getItems() {
		return this.types.clone();
	}


	public void setCountComparator(CountComparison countComparator) {
		this.countComparator = countComparator;
	}


	public CountComparison getCountComparator() {
		return this.countComparator;
	}


	public void setWhitelisted(boolean whitelisted) {
		this.whitelisted = whitelisted;
	}


	public boolean isWhitelisted() {
		return this.whitelisted;
	}


	public void setIgnoreNBT(boolean ignoreNBT) {
		this.ignoreNBT = ignoreNBT;
	}


	public boolean isIgnoreNBT() {
		return this.ignoreNBT;
	}


	@Override
	public int filter(ItemStack type, int amount) {
		if(ItemUtils.isEmpty(type))
			return 0;

		for(ItemStack stack : this.types) {
			if(ItemUtils.isEmpty(stack))
				continue;

			if(Items.getCustomItemFromStack(stack) instanceof ItemFilterItem filter) {
				if(!filter.test(type))
					continue;
			} else {
				if(this.ignoreNBT ? !ItemUtils.similar(stack, type) : !ItemUtils.equals(stack, type))
					continue;
			}

			if(!this.whitelisted)
				return 0;

			return this.countComparator.compare(amount, stack.getAmount());
		}

		return this.whitelisted ? 0 : amount;
	}


	@Override
	public String toString() {
		return String.format("CountComparison: %s, Whitelisted: %b, IgnoreNBT: %b, Types: %s", this.countComparator.name(), this.whitelisted, this.ignoreNBT, Arrays.toString(this.types));
	}

	/**
	 * Functional interface to compare the amount of items within filters.
	 */
	@FunctionalInterface
	private static interface CountComparator {

		/**
		 * Compares the amount of items of an {@link ItemStack} with the amount specified in the filter and returns the amount of items filtered.
		 * 
		 * @param amount     the amount of the stack to be filtered
		 * @param configured the amount specified in the filter
		 * @return the filtered amount
		 */
		int compare(int amount, int configured);

	}

	public static enum CountComparison implements CountComparator, Supplier<ItemStack> {

		IGNORE((n, x) -> n, new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(HeadTextures.WOODEN_MINUS).build()),
		EXACT((n, x) -> n < x ? 0 : x, new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(HeadTextures.WOODEN_EQUAL).build()),
		LESS((n, x) -> n < x ? n : 0, new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(HeadTextures.WOODEN_LESS_THAN).build()),
		MORE((n, x) -> n > x ? n : 0, new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(HeadTextures.WOODEN_GREATER_THAN).build());

		private final CountComparator comparator;
		private final ItemStack icon;

		private CountComparison(CountComparator comparator, ItemStack icon) {
			this.comparator = comparator;
			this.icon = icon;
		}


		@Override
		public int compare(int amount, int configured) {
			return this.comparator.compare(amount, configured);
		}


		@Override
		public ItemStack get() {
			return this.icon;
		}

	}

}
