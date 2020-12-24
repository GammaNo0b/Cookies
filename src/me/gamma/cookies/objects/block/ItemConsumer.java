
package me.gamma.cookies.objects.block;


import java.util.List;

import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.IItemSupplier;
import me.gamma.cookies.objects.recipe.CookieRecipe;



public interface ItemConsumer extends ItemHandler {

	List<IItemSupplier> getInputStackHolders(TileState block);


	default ItemStack addItem(TileState block, ItemStack stack) {
		for(IItemSupplier supplier : getInputStackHolders(block)) {
			ItemStack current = supplier.get();
			if(CookieRecipe.sameIngredient(current, stack)) {
				int canstore = current.getMaxStackSize() - current.getAmount();
				canstore = Math.min(canstore, stack.getAmount());
				current.setAmount(current.getAmount() + canstore);
				stack.setAmount(stack.getAmount() - canstore);
				if(stack.getAmount() == 0) {
					return null;
				}
			}
		}
		return stack;
	}

}
