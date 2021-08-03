
package me.gamma.cookies.objects.block.skull.machine;


import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;



public class AndroidCore extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.ANDROID_CORE;
	}


	@Override
	public String getRegistryName() {
		return "android_core";
	}


	@Override
	public String getDisplayName() {
		return "§aAndroid Core";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ELECTRIC_COMPONENTS, RecipeType.ENGINEER);
		recipe.setShape("SHS", "WMW", "CAC");
		recipe.setIngredient('S', CustomItemSetup.STEEL_INGOT.createDefaultItemStack());
		recipe.setIngredient('A', CustomItemSetup.ALUMINUM_INGOT.createDefaultItemStack());
		recipe.setIngredient('H', CustomBlockSetup.COPPER_COIL.createDefaultItemStack());
		recipe.setIngredient('W', CustomItemSetup.COPPER_WIRE.createDefaultItemStack());
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('C', CustomBlockSetup.ELECTRICAL_CIRCUIT.createDefaultItemStack());
		return recipe;
	}

}
