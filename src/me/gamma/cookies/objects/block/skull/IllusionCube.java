package me.gamma.cookies.objects.block.skull;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;


public class IllusionCube extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.ILLUSION_CUBE;
	}


	@Override
	public String getRegistryName() {
		return "illusion_cube";
	}


	@Override
	public String getDisplayName() {
		return "§5Illusion Cube";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.FUN, RecipeType.CUSTOM);
		recipe.setShape(" G ", "BCB", " P ");
		recipe.setIngredient('G', Material.GREEN_DYE);
		recipe.setIngredient('B', Material.BLACK_DYE);
		recipe.setIngredient('P', Material.PURPLE_DYE);
		recipe.setIngredient('C', CustomBlockSetup.CUBE.createDefaultItemStack());
		return recipe;
	}

}
