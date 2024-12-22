
package me.gamma.cookies.object.item;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.property.BigItemStackProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.RecipeUtils;



public interface ItemProvider extends Provider<ItemStack>, IItemSupplier {

	/**
	 * Same as {@link ItemProvider#get(int)} with {@link ItemStack#getMaxStackSize()} as argument.
	 * 
	 * @return the fetched item
	 */
	@Override
	default ItemStack get() {
		return get(this);
	}


	/**
	 * Static variant of {@link ItemProvider#get()}
	 * 
	 * @param provider the provider
	 * @return the fetched item
	 */
	static ItemStack get(Provider<ItemStack> provider) {
		ItemStack type = provider.getType();
		if(ItemUtils.isEmpty(type))
			return null;

		ItemStack stack = type.clone();
		stack.setAmount(provider.get(stack.getMaxStackSize()));
		return stack;
	}


	/**
	 * Same as {@link ItemProvider#get(Provider)} but with the option to specifiy the maximum amount of items to extract.
	 * 
	 * @param provider the provider
	 * @param amount   the amount to fetch
	 * @return the fetched item
	 */
	static ItemStack get(Provider<ItemStack> provider, int amount) {
		ItemStack type = provider.getType();
		if(ItemUtils.isEmpty(type))
			return null;

		ItemStack stack = type.clone();
		stack.setAmount(provider.get(amount));
		return stack;
	}


	/**
	 * Returns the same as {@link ItemProvider#get()} but does not extract any items from this provider.
	 * 
	 * @return the stack
	 */
	default ItemStack getStack() {
		return getStack(this);
	}


	/**
	 * Static variant of {@link ItemProvider#getStack()}.
	 * 
	 * @param provider the provider
	 * @return the stack
	 */
	static ItemStack getStack(Provider<ItemStack> provider) {
		ItemStack type = provider.getType();
		if(ItemUtils.isEmpty(type))
			return null;

		ItemStack stack = type.clone();
		stack.setAmount(Math.min(stack.getMaxStackSize(), provider.amount()));
		return stack;
	}


	@Override
	default boolean match(ItemStack type) {
		return ItemUtils.equals(this.getType(), type);
	}


	/**
	 * Checks if the given ingredient matches with the type of this provider.
	 * 
	 * @param ingredient the recipe choice ingredient
	 * @return if the types match
	 */
	default boolean match(RecipeChoice ingredient) {
		for(ItemStack stack : RecipeUtils.getItemsFromChoice(ingredient))
			if(this.match(stack))
				return true;
		return false;
	}


	/**
	 * Creates an {@link ItemProvider} from the given {@link TileState} using the {@link ItemStackProperty}.
	 * 
	 * @param property the property
	 * @param block    the block
	 * @return the created item provider
	 */
	public static ItemProvider fromItemStackProperty(final ItemStackProperty property, final TileState block) {
		return new ItemProvider() {

			@Override
			public int amount() {
				ItemStack stack = property.fetch(block);
				if(ItemUtils.isEmpty(stack))
					return 0;
				return stack.getAmount();
			}


			@Override
			public int capacity() {
				ItemStack stack = property.fetch(block);
				if(ItemUtils.isEmpty(stack))
					return 64;
				return stack.getMaxStackSize();
			}


			@Override
			public void add(ItemStack type, int amount) {
				ItemStack stack = property.fetch(block);
				if(stack == null) {
					stack = type.clone();
					stack.setAmount(0);
				}
				stack.setAmount(stack.getAmount() + amount);
				property.store(block, stack);
				block.update();
			}


			@Override
			public void remove(int amount) {
				ItemStack stack = property.fetch(block);
				stack.setAmount(Math.max(stack.getAmount() - amount, 0));
				property.store(block, stack);
				block.update();
			}


			@Override
			public ItemStack getType() {
				return property.fetch(block).clone();
			}


			@Override
			public void setType(ItemStack type) {
				ItemStack stack = type.clone();
				stack.setAmount(this.amount());
				property.store(block, stack);
				block.update();
			}


			@Override
			public boolean canChangeType(ItemStack type) {
				return true;
			}

		};
	}


	/**
	 * Creates an {@link ItemProvider} from the given {@link BigItemStack}.
	 * 
	 * @param stack the big item stack
	 * @return the created item provider
	 */
	public static ItemProvider fromBigItemStack(final BigItemStack stack) {
		return new ItemProvider() {

			@Override
			public int amount() {
				return stack.getAmount();
			}


			@Override
			public int capacity() {
				return Integer.MAX_VALUE;
			}


			@Override
			public void add(ItemStack type, int amount) {
				stack.grow(amount);
			}


			@Override
			public void remove(int amount) {
				stack.shrink(amount);
			}


			@Override
			public boolean isEmpty() {
				return stack.isEmpty();
			}


			@Override
			public ItemStack getType() {
				return stack.getStack().clone();
			}


			@Override
			public void setType(ItemStack type) {
				stack.setStack(type);
			}


			@Override
			public boolean canChangeType(ItemStack type) {
				return true;
			}

		};
	}


	/**
	 * Creates an {@link ItemProvider} from the given {@link PersistentDataHolder} using the {@link BigItemStackProperty}.
	 * 
	 * @param property the property
	 * @param holder   the holder
	 * @return the created item provider
	 */
	public static ItemProvider fromBigItemStackProperty(final BigItemStackProperty property, final PersistentDataHolder holder) {
		return fromBigItemStackProperty(property, holder, Integer.MAX_VALUE);
	}


	/**
	 * Creates an {@link ItemProvider} from the given {@link PersistentDataHolder} using the {@link BigItemStackProperty}.
	 * 
	 * @param property the property
	 * @param holder   the holder
	 * @param capacity the maximum stack size of the item
	 * @return the created item provider
	 */
	public static ItemProvider fromBigItemStackProperty(final BigItemStackProperty property, final PersistentDataHolder holder, final int capacity) {
		return new ItemProvider() {

			@Override
			public int amount() {
				return property.fetch(holder).getAmount();
			}


			@Override
			public int capacity() {
				return capacity;
			}


			@Override
			public void add(ItemStack type, int amount) {
				BigItemStack stack = property.fetch(holder);
				stack.grow(amount);
				property.store(holder, stack);
				if(holder instanceof TileState block)
					block.update();
			}


			@Override
			public void remove(int amount) {
				BigItemStack stack = property.fetch(holder);
				stack.shrink(amount);
				property.store(holder, stack);
				if(holder instanceof TileState block)
					block.update();
			}


			@Override
			public boolean isEmpty() {
				return property.fetch(holder).isEmpty();
			}


			@Override
			public ItemStack getType() {
				return property.fetch(holder).getStack();
			}


			@Override
			public void setType(ItemStack type) {
				BigItemStack stack = property.fetch(holder);
				stack.setStack(type);
				property.store(holder, stack);
				if(holder instanceof TileState block)
					block.update();
			}


			@Override
			public boolean canChangeType(ItemStack type) {
				return true;
			}

		};
	}


	/**
	 * Creates an {@link ItemProvider} from the given {@link Inventory} at the given slot.
	 * 
	 * @param inventory the inventory
	 * @param slot      the slot in the inventory
	 * @return the created item provider
	 */
	public static ItemProvider fromInventory(final Inventory inventory, final int slot) {
		return fromInventory(inventory, slot, Filter.empty());
	}


	/**
	 * Creates an {@link ItemProvider} from the given {@link Inventory} at the given slot that only accepts the given type.
	 * 
	 * @param inventory the inventory
	 * @param slot      the slot in the inventory
	 * @param type      the type
	 * @return the created item provider
	 */
	public static ItemProvider fromInventory(final Inventory inventory, final int slot, final Material type) {
		return fromInventory(inventory, slot, (stack, amount) -> ItemUtils.isType(stack, type) ? amount : 0);
	}


	/**
	 * Creates an {@link ItemProvider} from the given {@link Inventory} at the given slot that only accepts types fulfilling the given tiler.
	 * 
	 * @param inventory the inventory
	 * @param slot      the slot in the inventory
	 * @param filter    the filter
	 * @return the created item provider
	 */
	public static ItemProvider fromInventory(final Inventory inventory, final int slot, final Filter<ItemStack> filter) {
		return new ItemProvider() {

			@Override
			public int amount() {
				ItemStack stack = inventory.getItem(slot);
				return ItemUtils.isEmpty(stack) ? 0 : stack.getAmount();
			}


			@Override
			public int capacity() {
				ItemStack stack = inventory.getItem(slot);
				if(ItemUtils.isEmpty(stack))
					return 64;
				return stack.getMaxStackSize();
			}


			@Override
			public void add(ItemStack type, int amount) {
				if(amount <= 0)
					return;

				int size = this.amount();
				ItemStack stack = type.clone();
				stack.setAmount(size + amount);
				inventory.setItem(slot, stack);
			}


			public void remove(int amount) {
				if(amount <= 0)
					return;

				ItemStack stack = inventory.getItem(slot);
				if(!ItemUtils.isEmpty(stack))
					stack.setAmount(stack.getAmount() - amount);
			}


			@Override
			public boolean isEmpty() {
				return ItemUtils.isEmpty(inventory.getItem(slot));
			}


			@Override
			public ItemStack getType() {
				ItemStack type = inventory.getItem(slot);
				return ItemUtils.isEmpty(type) ? null : type.clone();
			}


			@Override
			public void setType(ItemStack type) {
				ItemStack stack = type.clone();
				stack.setAmount(this.amount());
				inventory.setItem(slot, stack);
			}


			@Override
			public boolean canChangeType(ItemStack type) {
				return filter.filter(type, 1) > 0;
			}

		};
	}


	/**
	 * Creates a list of {@link ItemProvider} from the given {@link Inventory} at all the given slots.
	 * 
	 * @param inventory the inventory
	 * @param slots     the slots in the inventory
	 * @return the list of item providers
	 */
	public static List<Provider<ItemStack>> fromInventory(Inventory inventory, int... slots) {
		ArrayList<Provider<ItemStack>> providers = new ArrayList<>();
		for(int slot : slots)
			providers.add(fromInventory(inventory, slot));
		return providers;
	}


	/**
	 * Creates a list of {@link ItemProvider} from the given {@link Inventory} at all the given slots.
	 * 
	 * @param inventory the inventory
	 * @param slots     the slots in the inventory
	 * @return the list of item providers
	 */
	public static List<Provider<ItemStack>> fromInventory(Inventory inventory, Iterable<? extends Integer> slots) {
		ArrayList<Provider<ItemStack>> providers = new ArrayList<>();
		for(int slot : slots)
			providers.add(fromInventory(inventory, slot));
		return providers;
	}


	/**
	 * Creates a list of {@link ItemProvider} from the given inventory.
	 * 
	 * @param inventory the inventory
	 * @return the list of item providers
	 */
	public static List<Provider<ItemStack>> fromInventory(Inventory inventory) {
		List<Provider<ItemStack>> providers = new ArrayList<>(inventory.getSize());
		for(int i = 0; i < inventory.getSize(); i++)
			providers.add(fromInventory(inventory, i));
		return providers;
	}

}
