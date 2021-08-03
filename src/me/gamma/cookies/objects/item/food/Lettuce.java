package me.gamma.cookies.objects.item.food;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.ShowcaseRecipe;


public class Lettuce extends AbstractCustomItem {

	@Override
	public String getRegistryName() {
		return "lettuce";
	}


	@Override
	public String getDisplayName() {
		return "§fLettuce";
	}


	@Override
	public Material getMaterial() {
		return Material.BEETROOT;
	}
	
	
	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.LETTUCE;
	}


	@Override
	public Recipe getRecipe() {
		return new ShowcaseRecipe(this.createDefaultItemStack(), RecipeCategory.PLANTS);
	}

}
