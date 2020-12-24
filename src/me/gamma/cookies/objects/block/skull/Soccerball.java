package me.gamma.cookies.objects.block.skull;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class Soccerball extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.SOCCERBALL;
	}


	@Override
	public String getRegistryName() {
		return "soccerball";
	}


	@Override
	public String getDisplayName() {
		return "§fSoccerball";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.FUN, RecipeType.CUSTOM);
		recipe.setShape(" B ", "BWB", " B ");
		recipe.setIngredient('B', Material.BLACK_DYE);
		recipe.setIngredient('W', Material.WHITE_WOOL);
		return recipe;
	}

}
