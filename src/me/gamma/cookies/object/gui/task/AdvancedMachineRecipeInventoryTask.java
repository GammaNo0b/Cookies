
package me.gamma.cookies.object.gui.task;


import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.recipe.machine.AdvancedMachineRecipe;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utils;



public class AdvancedMachineRecipeInventoryTask extends RecipeInventoryTask<AdvancedMachineRecipe> {

	public AdvancedMachineRecipeInventoryTask(Inventory inventory, ResultChoice resultchoice, AdvancedMachineRecipe recipe) {
		super(inventory, resultchoice, recipe);
	}


	@Override
	protected void initInventory() {
		this.inventory.setItem(22, new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("§9--- §3" + Utils.formatTicks(this.recipe.getDuration()) + " §9-->").build());
		this.inventory.setItem(24, this.recipe.getResult());
	}


	@Override
	protected void updateInventory() {
		int count = this.recipe.getIngredients().length;
		for(int i = 0; i < count; i++) {
			int r = i / 3;
			int c = i - 3 * r;
			this.inventory.setItem(r * 9 + c + 10, this.getItemFromItemChoice(this.recipe.getIngredients()[i]));
		}
	}

}
