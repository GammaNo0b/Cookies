
package me.gamma.cookies.object.gui.task;


import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;

import me.gamma.cookies.util.InventoryUtils;



public class ShapelessRecipeInventoryTask extends RecipeInventoryTask<ShapelessRecipe> {

	public ShapelessRecipeInventoryTask(Inventory inventory, ResultChoice resultchoice, ShapelessRecipe recipe) {
		super(inventory, resultchoice, recipe);
	}


	@Override
	protected void updateInventory() {
		int i = 0;
		for(RecipeChoice ingredient : this.recipe.getChoiceList()) {
			int column = i % 3;
			int row = (i - column) / 3 + 1;
			this.inventory.setItem(row * 9 + column + 1, this.getItemFromItemChoice(ingredient));
			i++;
		}
		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		while(i < 9) {
			int column = i % 3;
			int row = (i - column) / 3 + 1;
			this.inventory.setItem(row * 9 + column + 1, filler);
			i++;
		}
	}

}
