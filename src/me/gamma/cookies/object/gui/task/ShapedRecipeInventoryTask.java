
package me.gamma.cookies.object.gui.task;


import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import me.gamma.cookies.util.InventoryUtils;



public class ShapedRecipeInventoryTask extends RecipeInventoryTask<ShapedRecipe> {

	public ShapedRecipeInventoryTask(Inventory inventory, ResultChoice resultchoice, ShapedRecipe recipe) {
		super(inventory, resultchoice, recipe);
	}


	@Override
	protected void updateInventory() {
		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		Map<Character, RecipeChoice> ingredientMap = this.recipe.getChoiceMap();
		String[] shape = this.recipe.getShape();
		for(int i = 0; i < shape.length; i++) {
			String str = shape[i];
			for(int j = 0; j < str.length(); j++) {
				char c = str.charAt(j);
				ItemStack stack = this.getItemFromItemChoice(ingredientMap.get(c));
				this.inventory.setItem(i * 9 + j + 10, stack == null ? filler : stack);
			}
		}
	}

}
