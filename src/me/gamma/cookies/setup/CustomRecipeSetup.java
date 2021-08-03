
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
		registerRecipe(new CustomRecipe(new ItemStack(Material.COBBLESTONE), RecipeCategory.RESOURCES, RecipeType.CUSTOM).setShape("PP", "PP").setIngredient('P', CustomBlockSetup.STONE_PEBBLE.createDefaultItemStack()));
		registerRecipe(new CustomRecipe(new ItemStack(Material.GRANITE), RecipeCategory.RESOURCES, RecipeType.CUSTOM).setShape("PP", "PP").setIngredient('P', CustomBlockSetup.GRANITE_PEBBLE.createDefaultItemStack()));
		registerRecipe(new CustomRecipe(new ItemStack(Material.DIORITE), RecipeCategory.RESOURCES, RecipeType.CUSTOM).setShape("PP", "PP").setIngredient('P', CustomBlockSetup.DIORITE_PEBBLE.createDefaultItemStack()));
		registerRecipe(new CustomRecipe(new ItemStack(Material.ANDESITE), RecipeCategory.RESOURCES, RecipeType.CUSTOM).setShape("PP", "PP").setIngredient('P', CustomBlockSetup.ANDESITE_PEBBLE.createDefaultItemStack()));
		registerRecipe(new CustomRecipe(new ItemStack(Material.SOUL_SAND), RecipeCategory.RESOURCES, RecipeType.CUSTOM).setShape("dd", "dd").setIngredient('d', CustomItemSetup.SOUL_DUST.createDefaultItemStack()));
		registerRecipe(new CustomRecipe(new ItemStack(Material.SOUL_SOIL), RecipeCategory.RESOURCES, RecipeType.CUSTOM).setShape(" d ", "ddd", " d ").setIngredient('d', CustomItemSetup.SOUL_DUST.createDefaultItemStack()));
		registerRecipe(new CustomRecipe(new ItemStack(Material.ELYTRA), RecipeCategory.ARMOR, RecipeType.CUSTOM).setShape("CDC", "M M", "M M").setIngredient('C', Material.CHORUS_FRUIT).setIngredient('D', Material.DIAMOND).setIngredient('M', Material.PHANTOM_MEMBRANE));
	}


	private static void registerRecipe(Recipe recipe) {
		RecipeManager.registerRecipe(recipe);
	}

}
