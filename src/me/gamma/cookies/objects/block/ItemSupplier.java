
package me.gamma.cookies.objects.block;


import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.recipe.CookieRecipe;



public interface ItemSupplier extends ItemHandler {

	List<ItemProvider> getOutputStackHolders(TileState block);


	default ItemStack removeItem(TileState block) {
		ItemStack stack = null;
		for(ItemProvider supplier : getOutputStackHolders(block)) {
			ItemStack current = supplier.get();
			if(current != null && current.getType() != Material.AIR && current.getAmount() > 0) {
				if(stack == null) {
					stack = current.clone();
					current.setAmount(0);
				} else if(CookieRecipe.sameIngredient(current, stack)) {
					int canstore = stack.getType().getMaxStackSize() - stack.getAmount();
					canstore = Math.min(canstore, current.getAmount());
					current.setAmount(current.getAmount() - canstore);
					stack.setAmount(stack.getAmount() + canstore);
				}
			}
		}
		return stack;
	}
	
	
	default ItemStack removeItem(TileState block, ItemStack type) {
		ItemStack stack = null;
		for(ItemProvider supplier : getOutputStackHolders(block)) {
			ItemStack current = supplier.get();
			if(current != null && current.getType() != Material.AIR && current.getAmount() > 0) {
				if(stack == null) {
					stack = current.clone();
					current.setAmount(0);
				} else if(CookieRecipe.sameIngredient(current, stack)) {
					int canstore = stack.getType().getMaxStackSize() - stack.getAmount();
					canstore = Math.min(canstore, current.getAmount());
					current.setAmount(current.getAmount() - canstore);
					stack.setAmount(stack.getAmount() + canstore);
				}
			}
		}
		return stack;
	}

}
