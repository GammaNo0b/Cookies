
package me.gamma.cookies.objects.block;


import java.util.List;

import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.recipe.CookieRecipe;



public interface ItemConsumer extends ItemHandler {

	List<ItemProvider> getInputStackHolders(TileState block);


	default ItemStack addItem(TileState block, ItemStack stack) {
		for(ItemProvider consumer : getInputStackHolders(block)) {
			ItemStack current = consumer.get();
			if(current == null) {
				consumer.set(stack.clone());
				return null;
			} else if(CookieRecipe.sameIngredient(current, stack)) {
				int canstore = stack.getType().getMaxStackSize() - current.getAmount();
				canstore = Math.min(canstore, stack.getAmount());
				current.setAmount(current.getAmount() + canstore);
				consumer.set(current);
				stack.setAmount(stack.getAmount() - canstore);
				if(stack.getAmount() == 0) {
					return null;
				}
			}
		}
		return stack;
	}

}
