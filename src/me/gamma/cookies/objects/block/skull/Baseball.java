package me.gamma.cookies.objects.block.skull;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class Baseball extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.BASEBALL;
	}


	@Override
	public String getRegistryName() {
		return "baseball";
	}


	@Override
	public String getDisplayName() {
		return "§cBaseball";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.FUN, RecipeType.CUSTOM);
		recipe.setShape(" R ", "WSW", " W ");
		recipe.setIngredient('R', Material.RED_DYE);
		recipe.setIngredient('W', Material.WHITE_DYE);
		recipe.setIngredient('S', Material.SLIME_BALL);
		return recipe;
	}

}
