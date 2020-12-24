package me.gamma.cookies.objects.block.skull;

import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.Utilities;



public class RainbowCube extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.RAINBOW_CUBE;
	}


	@Override
	public String getRegistryName() {
		return "rainbow_cube";
	}


	@Override
	public String getDisplayName() {
		return Utilities.colorize("Rainbow Cube", "4c6ea23915d".toCharArray(), 1);
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.FUN, RecipeType.CUSTOM);
		recipe.setShape("CR");
		recipe.setIngredient('C', CustomBlockSetup.CUBE.createDefaultItemStack());
		recipe.setIngredient('R', CustomItemSetup.RAINBOW_DUST.createDefaultItemStack());
		return recipe;
	}
	
	

}
