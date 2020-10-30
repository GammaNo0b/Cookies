
package me.gamma.cookies.setup;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.managers.RecipeManager;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.objects.recipe.ShowcaseRecipe;



public class CustomRecipeSetup {

	public static void registerCustomRecipes() {
		registerRecipe(new ShowcaseRecipe(new ItemStack(Material.BEETROOT), RecipeCategory.PLANTS));
		registerRecipe(new CustomRecipe(new ItemStack(Material.GOLD_NUGGET), null, RecipeType.CUSTOM).setShape("ggg", "ggg", "ggg"));
	}


	private static void registerRecipe(Recipe recipe) {
		RecipeManager.registerRecipe(recipe);
	}

}
