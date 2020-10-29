package me.gamma.cookies.objects.block.skull;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class Cube extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.COLOR_FFFFFF;
	}


	@Override
	public String getIdentifier() {
		return "cube";
	}


	@Override
	public String getDisplayName() {
		return "§fCube";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), 8, RecipeCategory.RESOURCES, RecipeType.CUSTOM);
		recipe.setShape("B");
		recipe.setIngredient('B', Material.WHITE_CONCRETE);
		return recipe;
	}

}
