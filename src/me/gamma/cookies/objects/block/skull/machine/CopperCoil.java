package me.gamma.cookies.objects.block.skull.machine;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;


public class CopperCoil extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.COPPER_COIL;
	}


	@Override
	public String getRegistryName() {
		return "copper_coil";
	}


	@Override
	public String getDisplayName() {
		return "§6Copper Coil";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ELECTRIC_COMPONENTS, RecipeType.ENGINEER);
		recipe.setShape(" W ", "WSW", " W ");
		recipe.setIngredient('S', Material.STICK);
		recipe.setIngredient('W', CustomItemSetup.COPPER_WIRE.createDefaultItemStack());
		return recipe;
	}

}
