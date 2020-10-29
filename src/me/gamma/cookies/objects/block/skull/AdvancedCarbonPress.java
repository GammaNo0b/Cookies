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


public class AdvancedCarbonPress extends AdvancedMachine {

	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>(CustomBlockSetup.CARBON_PRESS.getMachineRecipes());
		return recipes;
	}


	@Override
	public ItemStack getProgressIcon() {
		return new ItemStack(Material.PISTON);
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CARBON_PRESS;
	}


	@Override
	public String getIdentifier() {
		return "advanced_carbon_press";
	}


	@Override
	public String getDisplayName() {
		return "§8Advanced Carbon Press";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Special Compression Machine that only compresses carbon related resources.", "§eSpeed: §6" + this.getSpeed());
	}
	
	
	@Override
	public int getSpeed() {
		return 4;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" S ", "MTM", "KCK");
		recipe.setIngredient('S', CustomItemSetup.STEEL_INGOT.createDefaultItemStack());
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('T', CustomBlockSetup.CARBON_PRESS.createDefaultItemStack());
		recipe.setIngredient('C', CustomBlockSetup.MACHINE_CASING.createDefaultItemStack());
		recipe.setIngredient('K', CustomItemSetup.COMPRESSED_CARBON.createDefaultItemStack());
		return recipe;
	}

}
