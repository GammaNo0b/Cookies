
package me.gamma.cookies.object.gui.task;


import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingRecipe;

import me.gamma.cookies.util.InventoryUtils;



public class SmithingRecipeInventoryTask extends RecipeInventoryTask<SmithingRecipe> {

	public SmithingRecipeInventoryTask(Inventory inventory, ResultChoice resultchoice, SmithingRecipe recipe) {
		super(inventory, resultchoice, recipe);
	}


	@Override
	protected void initInventory() {
		super.initInventory();

		ItemStack filler = InventoryUtils.filler(Material.BLACK_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			int column = i % 3;
			int row = (i - column) / 3;
			int slot = (row + 1) * 9 + (column + 1);
			this.inventory.setItem(slot, filler);
		}
		this.inventory.setItem(29, new ItemStack(Material.SMITHING_TABLE));
	}


	@Override
	protected void updateInventory() {
		this.inventory.setItem(11, this.getItemFromItemChoice(this.recipe.getAddition()));
		this.inventory.setItem(20, this.getItemFromItemChoice(this.recipe.getBase()));
	}

}
