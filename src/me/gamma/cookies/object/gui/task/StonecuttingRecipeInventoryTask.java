
package me.gamma.cookies.object.gui.task;


import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.StonecuttingRecipe;

import me.gamma.cookies.util.InventoryUtils;



public class StonecuttingRecipeInventoryTask extends RecipeInventoryTask<StonecuttingRecipe> {

	public StonecuttingRecipeInventoryTask(Inventory inventory, ResultChoice resultchoice, StonecuttingRecipe recipe) {
		super(inventory, resultchoice, recipe);
	}


	@Override
	protected void initInventory() {
		super.initInventory();

		ItemStack filler = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			int column = i % 3;
			int row = (i - column) / 3;
			int slot = (row + 1) * 9 + (column + 1);
			this.inventory.setItem(slot, filler);
		}
		this.inventory.setItem(29, new ItemStack(Material.STONECUTTER));
	}


	@Override
	protected void updateInventory() {
		this.inventory.setItem(20, this.getItemFromItemChoice(this.recipe.getInputChoice()));
	}

}
