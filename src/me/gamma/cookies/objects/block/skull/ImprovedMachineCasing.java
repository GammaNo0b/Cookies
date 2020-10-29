package me.gamma.cookies.objects.block.skull;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;


public class ImprovedMachineCasing extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.IMPROVED_MACHINE_CASING;
	}


	@Override
	public String getIdentifier() {
		return "improved_machine_casing";
	}


	@Override
	public String getDisplayName() {
		return "§cImproved Machine Casing";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Second best Machine Core.");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("LDL", "DCD", "LDL");
		recipe.setIngredient('L', Material.LAPIS_LAZULI);
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('C', CustomBlockSetup.ADVANCED_MACHINE_CASING.createDefaultItemStack());
		return recipe;
	}

}
