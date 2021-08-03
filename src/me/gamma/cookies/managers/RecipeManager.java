
package me.gamma.cookies.managers;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.AbstractTileStateBlock;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.recipe.CookieRecipe;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;



public class RecipeManager {

	private static final List<Recipe> registeredRecipes = new ArrayList<>();
	private static final List<CookieRecipe> registeredCookieRecipes = new ArrayList<>();

	public static void loadRecipes() {
		registerRecipes(CustomItemSetup.customItems.stream().map(AbstractCustomItem::getRecipe).filter(recipe -> recipe != null).collect(Collectors.toList()));
		registerRecipes(CustomBlockSetup.customBlocks.stream().map(AbstractTileStateBlock::getRecipes).collect(ArrayList<Recipe>::new, List::addAll, List::addAll).stream().filter(recipe -> recipe != null).collect(Collectors.toList()));
	}


	public static void registerRecipes(Iterable<Recipe> recipes) {
		recipes.forEach(RecipeManager::registerRecipe);
	}


	public static <R extends Recipe> R registerRecipe(R recipe) {
		registeredRecipes.add(recipe);
		if(recipe instanceof CookieRecipe) {
			registeredCookieRecipes.add((CookieRecipe) recipe);
		} else {
			Bukkit.addRecipe(recipe);
		}
		return recipe;
	}


	public static List<Recipe> getRegisteredRecipes() {
		return registeredRecipes;
	}


	public static List<CookieRecipe> getRegisteredCustomRecipes() {
		return registeredCookieRecipes;
	}


	public static List<CookieRecipe> getRegisteredCustomRecipes(RecipeType type) {
		return registeredCookieRecipes.stream().filter(recipe -> recipe.getType() == type).collect(Collectors.toList());
	}


	public static Recipe getRecipeFromResult(ItemStack result) {
		for(Recipe recipe : registeredRecipes) {
			if(CookieRecipe.sameIngredient(recipe.getResult(), result)) {
				return recipe;
			}
		}

		return null;
	}


	public static List<Recipe> getVariatedBukkitRecipesFor(ItemStack result) {
		return Bukkit.getRecipesFor(result);
	}


	public static List<Recipe> getAllRecipesFor(ItemStack result) {
		List<Recipe> recipes = new ArrayList<>();
		if(!AbstractCustomItem.isCustomItem(result))
			recipes.addAll(getVariatedBukkitRecipesFor(result));
		recipes.addAll(getCookieRecipesFromStack(result));
		return recipes;
	}


	public static List<CookieRecipe> getCookieRecipesFromStack(ItemStack stack) {
		return registeredCookieRecipes.stream().filter(recipe -> CookieRecipe.sameIngredient(stack, recipe.getResult())).collect(Collectors.toList());
	}

}
