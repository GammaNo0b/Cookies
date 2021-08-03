package me.gamma.cookies.objects.item.resources;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;


public class AluminumFoil extends AbstractCustomItem {

	@Override
	public String getRegistryName() {
		return "aluminum_foil";
	}


	@Override
	public String getDisplayName() {
		return "§fAluminum Foil";
	}


	@Override
	public Material getMaterial() {
		return Material.PAPER;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), 8, RecipeCategory.RESOURCES, RecipeType.ENGINEER);
		recipe.setShape("AA");
		recipe.setIngredient('A', CustomItemSetup.ALUMINUM_INGOT.createDefaultItemStack());
		return recipe;
	}

}
