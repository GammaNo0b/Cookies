
package me.gamma.cookies.object.block;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.item.BigItemStack;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemStorage;
import me.gamma.cookies.object.property.BigItemStackProperty;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.util.math.MathHelper;



public interface BigItemStorage extends ItemStorage {

	IntegerProperty STORAGE_CAPACITY = Properties.STORAGE_CAPACITY;

	/**
	 * Returns the amount of different items this big item storage can store.
	 * 
	 * @return amount of items
	 */
	int getCapacity();


	/**
	 * Creates the array of {@link BigItemStackProperty} for this {@link BigItemStorage}.
	 * 
	 * @return the array
	 */
	default BigItemStackProperty[] createItemProperties() {
		return createProperties(this.getCapacity());
	}


	/**
	 * Creates a new {@link BigItemStackProperty} with the given index.
	 * 
	 * @param index the index
	 * @return the created property
	 */
	static BigItemStackProperty createProperty(int index) {
		return new BigItemStackProperty("Item" + index);
	}


	/**
	 * Creates an array of {@link BigItemStackProperty} for a {@link BigItemStorage} with the given capacity.
	 * 
	 * @param capacity the capacity
	 * @return the array
	 */
	static BigItemStackProperty[] createProperties(int capacity) {
		BigItemStackProperty[] properties = new BigItemStackProperty[capacity];
		for(int i = 0; i < properties.length; i++)
			properties[i] = createProperty(i);
		return properties;
	}


	/**
	 * Creates an array of {@link BigItemStackProperty} for the given block.
	 * 
	 * @param holder the block
	 * @return the array
	 */
	static BigItemStackProperty[] createProperties(PersistentDataHolder holder) {
		return createProperties(STORAGE_CAPACITY.fetch(holder));
	}


	@Override
	default List<Provider<ItemStack>> getItemProviders(PersistentDataHolder holder) {
		return Arrays.stream(createProperties(holder)).map(property -> ItemProvider.fromBigItemStackProperty(property, holder)).collect(Collectors.toList());
	}


	/**
	 * Requests the item stack from the given block at the given index.
	 * 
	 * @param block the block
	 * @param index the index
	 * @return the item stack
	 */
	default ItemStack requestItem(TileState block, int index) {
		index %= this.getCapacity();
		BigItemStackProperty property = createProperty(index);
		BigItemStack stack = property.fetch(block);
		int amount = MathHelper.min(stack.getStack().getType().getMaxStackSize(), stack.getAmount());
		stack.shrink(amount);
		property.store(block, stack);
		block.update();
		ItemStack result = stack.getStack().clone();
		result.setAmount(amount);
		return result;
	}

	// static ItemStack storeItem(TileState block, ItemStack stack) {
	// if(ItemUtils.isEmpty(stack))
	// return null;
	// return ItemConsumer.addStack(stack, createItemProviders(block));
	// }
	//
	//
	// static ItemStack requestItem(TileState block, ItemFilter filter) {
	// Holder<ItemStack> holder = new Holder<>();
	// ItemSupplier.removeItem(filter, createItemProviders(block), stack -> {
	// holder.set(stack);
	// return null;
	// });
	// return holder.get();
	// }
	//
	//
	// static ItemStack requestItem(TileState block, int index) {
	// index %= getCapacity(block);
	// BigItemStackProperty property = createProperty(index);
	// BigItemStack stack = property.fetch(block);
	// int amount = MathHelper.min(stack.getStack().getType().getMaxStackSize(), stack.getAmount());
	// stack.shrink(amount);
	// property.store(block, stack);
	// block.update();
	// ItemStack result = stack.getStack().clone();
	// result.setAmount(amount);
	// return result;
	// }


	static List<BigItemStack> getContent(TileState block) {
		List<BigItemStack> content = new ArrayList<>();
		for(BigItemStackProperty property : createProperties(block)) {
			BigItemStack stack = property.fetch(block);
			if(stack != null)
				content.add(stack);
			else
				content.add(BigItemStack.EMPTY);
		}
		return content;
	}

}
