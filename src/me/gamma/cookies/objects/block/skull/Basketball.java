package me.gamma.cookies.objects.block.skull;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class Basketball extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.BASKETBALL;
	}


	@Override
	public String getRegistryName() {
		return "basketball";
	}


	@Override
	public String getDisplayName() {
		return "§6Basketball";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.FUN, RecipeType.CUSTOM);
		recipe.setShape(" O ", "OBO", " O ");
		recipe.setIngredient('O', Material.ORANGE_DYE);
		recipe.setIngredient('B', Material.BLACK_WOOL);
		return recipe;
	}

}
