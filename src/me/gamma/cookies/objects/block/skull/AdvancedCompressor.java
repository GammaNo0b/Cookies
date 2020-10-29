package me.gamma.cookies.objects.block.skull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CompressionRecipe;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.MachineRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;


public class AdvancedCompressor extends AdvancedMachine {

	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>(CustomBlockSetup.COMPRESSOR.getMachineRecipes());
		recipes.add(new CompressionRecipe("coal_to_coal_block", new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL, 9), 120));
		recipes.add(new CompressionRecipe("iron_nugget_to_iron_ingot", new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_NUGGET, 9), 120));
		recipes.add(new CompressionRecipe("iron_to_iron_block", new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_INGOT, 9), 120));
		recipes.add(new CompressionRecipe("gold_nugget_to_gold_ingot", new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.GOLD_NUGGET, 9), 120));
		recipes.add(new CompressionRecipe("gold_to_gold_block", new ItemStack(Material.GOLD_BLOCK), new ItemStack(Material.GOLD_INGOT, 9), 120));
		recipes.add(new CompressionRecipe("redstone_to_redstone_block", new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.REDSTONE, 9), 120));
		recipes.add(new CompressionRecipe("lapis_to_lapis_block", new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_LAZULI, 9), 120));
		recipes.add(new CompressionRecipe("emerald_to_emerald_block", new ItemStack(Material.EMERALD_BLOCK), new ItemStack(Material.EMERALD, 9), 120));
		recipes.add(new CompressionRecipe("diamond_to_diamond_block", new ItemStack(Material.DIAMOND_BLOCK), new ItemStack(Material.DIAMOND, 9), 120));
		recipes.add(new CompressionRecipe("netherite_to_netherite_block", new ItemStack(Material.NETHERITE_BLOCK), new ItemStack(Material.NETHERITE_INGOT, 9), 120));
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
		return "advanced_compressor";
	}


	@Override
	public String getDisplayName() {
		return "§eAdvanced Compressor";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Compresses Materials to create Resources with higher density.", "§eSpeed: §6" + this.getSpeed());
	}
	
	
	@Override
	public int getSpeed() {
		return 4;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" S ", "PTP", "MCM");
		recipe.setIngredient('S', CustomItemSetup.STEEL_INGOT.createDefaultItemStack());
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('T', CustomBlockSetup.COMPRESSOR.createDefaultItemStack());
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('C', CustomBlockSetup.ADVANCED_MACHINE_CASING.createDefaultItemStack());
		return recipe;
	}

}
