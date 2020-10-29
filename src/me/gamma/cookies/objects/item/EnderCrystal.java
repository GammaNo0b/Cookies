package me.gamma.cookies.objects.item;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class EnderCrystal extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "ender_crystal";
	}


	@Override
	public String getDisplayName() {
		return "§3Ender Crystal";
	}


	@Override
	public Material getMaterial() {
		return Material.EMERALD;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.RESOURCES, RecipeType.CUSTOM);
		recipe.setShape(" E ", "PGP", " E ");
		recipe.setIngredient('E', Material.ENDER_EYE);
		recipe.setIngredient('P', Material.ENDER_PEARL);
		recipe.setIngredient('G', Material.EMERALD);
		return recipe;
	}

}
