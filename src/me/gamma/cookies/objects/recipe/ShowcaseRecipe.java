
package me.gamma.cookies.objects.recipe;


import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;



public class ShowcaseRecipe implements Recipe {

	private ItemStack result;

	public ShowcaseRecipe(ItemStack result, RecipeCategory category) {
		this.result = result;
		category.registerRecipe(this);
	}


	@Override
	public ItemStack getResult() {
		return this.result;
	}

}
