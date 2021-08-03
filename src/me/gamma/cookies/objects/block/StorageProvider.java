
package me.gamma.cookies.objects.block;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.ItemFilter;
import me.gamma.cookies.objects.block.skull.storage.StorageSkullBlock;
import me.gamma.cookies.objects.property.BigItemStackProperty;
import me.gamma.cookies.util.BigItemStack;
import me.gamma.cookies.util.MathHelper;



public interface StorageProvider {

	int getCapacity();


	default BigItemStackProperty[] createProperties() {
		BigItemStackProperty[] properties = new BigItemStackProperty[this.getCapacity()];
		for(int i = 0; i < properties.length; i++)
			properties[i] = new BigItemStackProperty("Item" + i);
		return properties;
	}


	static BigItemStackProperty createProperty(int index) {
		return new BigItemStackProperty("Item" + index);
	}


	static BigItemStackProperty[] createProperties(TileState block) {
		BigItemStackProperty[] properties = new BigItemStackProperty[getCapacity(block)];
		for(int i = 0; i < properties.length; i++)
			properties[i] = createProperty(i);
		return properties;
	}


	static ItemStack storeItem(TileState block, ItemStack stack) {
		if(stack == null) {
			return null;
		}
		for(BigItemStackProperty property : createProperties(block)) {
			BigItemStack bigstack = property.fetch(block);
			if(bigstack.isEmpty()) {
				property.store(block, new BigItemStack(stack, stack.getAmount()));
				block.update();
				return null;
			} else if(bigstack.isSimilar(stack)) {
				int store = stack.getAmount();
				int amount = bigstack.getAmount();
				int space = Integer.MAX_VALUE - amount;
				int canstore = Math.min(store, space);
				store -= canstore;
				bigstack.grow(canstore);
				property.store(block, bigstack);
				if(store == 0) {
					block.update();
					return null;
				}
				stack.setAmount(store);
			}
		}
		block.update();
		return stack;
	}


	static ItemStack requestItem(TileState block, ItemFilter filter) {
		ItemStack type = null;
		int amount = 0;
		for(BigItemStackProperty property : createProperties(block)) {
			BigItemStack bigstack = property.fetch(block);
			if(filter.matches(bigstack) && (type == null || bigstack.isSimilar(type))) {
				type = bigstack.getStack().clone();
				int stored = bigstack.getAmount();
				int canfetch = Math.min(stored, type.getMaxStackSize() - amount);
				bigstack.shrink(canfetch);
				if(bigstack.getAmount() == 0) {
					property.storeEmpty(block);
				} else {
					property.store(block, bigstack);
				}
				amount += canfetch;
				if(amount >= type.getMaxStackSize())
					break;
			}
		}
		block.update();
		if(type != null)
			type.setAmount(amount);
		return type;
	}


	static ItemStack requestItem(TileState block, int index) {
		index %= getCapacity(block);
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


	static int getCapacity(TileState block) {
		return StorageSkullBlock.STORAGE_CAPACITY.fetch(block);
	}

}
