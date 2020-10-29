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


public class ImprovedSmeltery extends ImprovedMachine {

	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>(CustomBlockSetup.ADVANCED_SMELTERY.getMachineRecipes());
		
		return recipes;
	}


	@Override
	public ItemStack getProgressIcon() {
		return new ItemStack(Material.FLINT_AND_STEEL);
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.SMELTERY;
	}


	@Override
	public String getIdentifier() {
		return "improved_smeltery";
	}


	@Override
	public String getDisplayName() {
		return "§cImproved Smeltery";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Smeltery that can alloy up to six materials.", "§eSpeed: §6" + this.getSpeed());
	}
	
	
	@Override
	public int getSpeed() {
		return 10;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" S ", "HBH", "SCS");
		recipe.setIngredient('H', CustomBlockSetup.COPPER_COIL.createDefaultItemStack());
		recipe.setIngredient('S', CustomItemSetup.STEEL_INGOT.createDefaultItemStack());
		recipe.setIngredient('C', CustomBlockSetup.IMPROVED_MACHINE_CASING.createDefaultItemStack());
		recipe.setIngredient('B', CustomBlockSetup.ADVANCED_SMELTERY.createDefaultItemStack());
		return recipe;
	}

}
