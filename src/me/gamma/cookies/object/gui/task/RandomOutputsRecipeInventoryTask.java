
package me.gamma.cookies.object.gui.task;


import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.recipe.machine.RandomOutputsRecipe;
import me.gamma.cookies.object.recipe.machine.RandomOutputsRecipe.Result;
import me.gamma.cookies.util.CollectionUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utils;



public class RandomOutputsRecipeInventoryTask extends RecipeInventoryTask<RandomOutputsRecipe> {

	public RandomOutputsRecipeInventoryTask(Inventory inventory, ResultChoice resultchoice, RandomOutputsRecipe recipe) {
		super(inventory, resultchoice, recipe);
	}


	@Override
	protected void initInventory() {
		this.inventory.setItem(22, new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("§9--- §3" + Utils.formatTicks(this.recipe.getDuration()) + " §9-->").build());
	}


	@Override
	protected void updateInventory() {
		if(this.recipe.getIngredients().length > 0)
			this.inventory.setItem(20, this.getItemFromItemChoice(this.recipe.getIngredients()[0]));
		List<Result> results = this.recipe.getResults();
		int pages = (results.size() + 8) / 9;
		List<Result> items = CollectionUtils.subList(results, (this.cycle % pages) * 9, 9);
		for(int i = 0; i < items.size(); i++) {
			int r = i / 3;
			int c = i - 3 * r;
			this.inventory.setItem(r * 9 + c + 14, items.get(i).createIcon(0));
		}
	}

}
