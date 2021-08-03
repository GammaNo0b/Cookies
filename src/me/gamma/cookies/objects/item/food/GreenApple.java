package me.gamma.cookies.objects.item.food;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.ShowcaseRecipe;


public class GreenApple extends AbstractCustomItem {

	@Override
	public String getRegistryName() {
		return "green_apple";
	}


	@Override
	public String getDisplayName() {
		return "§aGreen Apple";
	}


	@Override
	public Material getMaterial() {
		return Material.APPLE;
	}
	
	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.GREEN_APPLE;
	}


	@Override
	public Recipe getRecipe() {
		return new ShowcaseRecipe(createDefaultItemStack(), RecipeCategory.PLANTS);
	}

}
