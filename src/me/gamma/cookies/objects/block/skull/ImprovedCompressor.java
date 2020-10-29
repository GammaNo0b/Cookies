
package me.gamma.cookies.objects.block.skull;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.MachineRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;



public class ImprovedCompressor extends ImprovedMachine {

	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>(CustomBlockSetup.ADVANCED_COMPRESSOR.getMachineRecipes());

		return recipes;
	}


	@Override
	public ItemStack getProgressIcon() {
		return new ItemStack(Material.PISTON);
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.COMPRESSOR;
	}


	@Override
	public String getIdentifier() {
		return "improved_compressor";
	}


	@Override
	public String getDisplayName() {
		return "§eImproved Compressor";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Compresses Materials to create Resources with higher density.", "§eSpeed: §6" + this.getSpeed());
	}


	@Override
	public int getSpeed() {
		return 10;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" S ", "PTP", "MCM");
		recipe.setIngredient('S', CustomItemSetup.STEEL_INGOT.createDefaultItemStack());
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('T', CustomBlockSetup.ADVANCED_COMPRESSOR.createDefaultItemStack());
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('C', CustomBlockSetup.IMPROVED_MACHINE_CASING.createDefaultItemStack());
		return recipe;
	}

}
