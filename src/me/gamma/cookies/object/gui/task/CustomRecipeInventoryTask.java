
package me.gamma.cookies.object.gui.task;


import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import me.gamma.cookies.object.recipe.CustomRecipe;
import me.gamma.cookies.util.InventoryUtils;



public class CustomRecipeInventoryTask extends RecipeInventoryTask<CustomRecipe> {

	public CustomRecipeInventoryTask(Inventory inventory, ResultChoice resultchoice, CustomRecipe recipe) {
		super(inventory, resultchoice, recipe);
	}


	@Override
	protected void initInventory() {
		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		Map<Character, RecipeChoice> ingredientMap = this.recipe.getIngredientMap();
		String[] shape = this.recipe.getShape();
		for(int i = 0; i < shape.length; i++) {
			String str = shape[i];
			for(int j = 0; j < str.length(); j++) {
				char c = str.charAt(j);
				if(ingredientMap.get(c) == null)
					this.inventory.setItem(i * 9 + j + 10, filler);
			}
		}
	}


	@Override
	protected void updateInventory() {
		Map<Character, RecipeChoice> ingredientMap = this.recipe.getIngredientMap();
		String[] shape = this.recipe.getShape();
		for(int i = 0; i < shape.length; i++) {
			String str = shape[i];
			for(int j = 0; j < str.length(); j++) {
				char c = str.charAt(j);
				RecipeChoice choice = ingredientMap.get(c);
				if(choice != null) {
					this.inventory.setItem(i * 9 + j + 10, this.getItemFromItemChoice(choice));
				}
			}
		}
	}

}
