
package me.gamma.cookies.objects.block.skull.machine;


import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;



public class Electromagnet extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.MAGNET;
	}


	@Override
	public String getRegistryName() {
		return "electromagnet";
	}


	@Override
	public String getDisplayName() {
		return "§cElectromagnet";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ELECTRIC_COMPONENTS, RecipeType.ENGINEER);
		recipe.setShape(" I ", " I ", "ACA");
		recipe.setIngredient('I', Material.IRON_NUGGET);
		recipe.setIngredient('A', CustomItemSetup.ALUMINUM_FOIL.createDefaultItemStack());		recipe.setIngredient('C', CustomBlockSetup.COPPER_COIL.createDefaultItemStack());
		return recipe;
	}

}
