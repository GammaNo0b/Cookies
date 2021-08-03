
package me.gamma.cookies.objects.recipe;


import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;



public interface MachineRecipe extends CookieRecipe {
	
	String getIdentifier();

	int getDuration();

	ItemStack[] getIngredients();
	
	ItemStack[] getExtraResults();
	
	ItemStack createIcon();
	
	Inventory display(String title);
	
	@Override
	default RecipeType getType() {
		return RecipeType.MACHINE;
	}


	default boolean matches(ItemStack[] input) {
		Map<ItemStack, Integer> map = new HashMap<>();
		for(ItemStack stack : input) {
			if(stack != null) {
				stack = stack.clone();
				int amount = stack.getAmount();
				stack.setAmount(1);
				if(!map.containsKey(stack))
					map.put(stack, amount);
				else
					map.put(stack, amount + map.get(stack));
			}
		}
		if(this.getIngredients() == null)
			return false;
		for(ItemStack stack : this.getIngredients()) {
			stack = stack.clone();
			int amount = stack.getAmount();
			stack.setAmount(1);
			if(!map.containsKey(stack))
				return false;
			int available = map.get(stack);
			available -= amount;
			if(available < 0)
				return false;
			map.put(stack, available);
		}
		return true;
	}

}
