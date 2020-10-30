
package me.gamma.cookies.setup;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.managers.RecipeManager;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.ShowcaseRecipe;



public class CustomRecipeSetup {

	public static void registerCustomRecipes() {
		registerRecipe(new ShowcaseRecipe(new ItemStack(Material.BEETROOT), RecipeCategory.PLANTS));
	}


	private static void registerRecipe(Recipe recipe) {
		RecipeManager.registerRecipe(recipe);
	}

}
