package me.gamma.cookies.objects.block.skull.machine;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;


public class Accumulator extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.ACCUMULATOR;
	}


	@Override
	public String getRegistryName() {
		return "accumulator";
	}


	@Override
	public String getDisplayName() {
		return "§2Accumulator";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ELECTRIC_COMPONENTS, RecipeType.ENGINEER);
		recipe.setShape("ASA", "ARA", "ACA");
		recipe.setIngredient('S', CustomItemSetup.SILVER_INGOT.createDefaultItemStack());
		recipe.setIngredient('C', CustomItemSetup.COPPER_INGOT.createDefaultItemStack());
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('A', CustomItemSetup.ALUMINUM_FOIL.createDefaultItemStack());
		return recipe;
	}

}
