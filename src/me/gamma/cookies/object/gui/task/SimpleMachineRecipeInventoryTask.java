
package me.gamma.cookies.object.gui.task;


import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.recipe.machine.SimpleMachineRecipe;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utils;



public class SimpleMachineRecipeInventoryTask extends RecipeInventoryTask<SimpleMachineRecipe> {

	public SimpleMachineRecipeInventoryTask(Inventory inventory, ResultChoice resultchoice, SimpleMachineRecipe recipe) {
		super(inventory, resultchoice, recipe);
	}


	@Override
	protected void initInventory() {
		this.inventory.setItem(22, new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("§9--- §3" + Utils.formatTicks(this.recipe.getDuration()) + " §9-->").build());
		this.inventory.setItem(24, this.recipe.getResult());
	}


	@Override
	protected void updateInventory() {
		this.inventory.setItem(20, this.getItemFromItemChoice(this.recipe.getIngredients()[0]));
	}

}
