
package me.gamma.cookies.object.gui.task;


import org.bukkit.Material;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;



public class CookingRecipeInventoryTask<R extends CookingRecipe<R>> extends RecipeInventoryTask<R> {

	protected final int coalticks;
	protected final Material fuel;
	protected final Material background;

	public CookingRecipeInventoryTask(Inventory inventory, ResultChoice resultchoice, R recipe, Material fuel, Material background) {
		this(inventory, resultchoice, recipe, 0, fuel, background);
	}


	public CookingRecipeInventoryTask(Inventory inventory, ResultChoice resultchoice, R recipe, int coalticks, Material fuel, Material background) {
		super(inventory, resultchoice, recipe);
		this.coalticks = coalticks;
		this.fuel = fuel;
		this.background = background;
	}


	@Override
	protected void initInventory() {
		ItemStack filler = InventoryUtils.filler(this.background);
		for(int i = 0; i < 9; i++) {
			int column = i % 3;
			int row = (i - column) / 3;
			int slot = (row + 1) * 9 + (column + 1);
			this.inventory.setItem(slot, filler);
		}

		int burntime = this.recipe.getCookingTime();
		StringBuilder name = new StringBuilder("§eCooking Time§8: §c");
		name.append(burntime);
		if(this.coalticks > 0) {
			name.append(" §8| §6");
			name.append(this.coalticks / burntime);
			name.append(" §aItems §6per §8Coal");
		}
		this.inventory.setItem(29, new ItemBuilder(this.fuel).setName(name.toString()).build());
	}


	@Override
	protected void updateInventory() {
		this.inventory.setItem(20, this.getItemFromItemChoice(this.recipe.getInputChoice()));
	}

}
