package me.gamma.cookies.objects.block.skull;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class Golfball extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.GOLFBALL;
	}


	@Override
	public String getRegistryName() {
		return "golfball";
	}


	@Override
	public String getDisplayName() {
		return "�fGolfball";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.FUN, RecipeType.CUSTOM);
		recipe.setShape(" W ", "WSW", " W ");
		recipe.setIngredient('W', Material.WHITE_DYE);
		recipe.setIngredient('S', Material.SLIME_BALL);
		return recipe;
	}

}
