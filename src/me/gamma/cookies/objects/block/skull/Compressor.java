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


public class Compressor extends BasicMachine {

	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>();
		recipes.add(new CompressionRecipe("wheat_to_hay_block", new ItemStack(Material.HAY_BLOCK), new ItemStack(Material.WHEAT, 9), 60));
		recipes.add(new CompressionRecipe("melons_to_block", new ItemStack(Material.MELON), new ItemStack(Material.MELON_SLICE, 9), 80));
		recipes.add(new CompressionRecipe("dried_kelp_to_block", new ItemStack(Material.DRIED_KELP_BLOCK), new ItemStack(Material.DRIED_KELP, 9), 60));
		recipes.add(new CompressionRecipe("string_to_wool", new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.STRING, 4), 80));
		recipes.add(new CompressionRecipe("glowstone_to_block", new ItemStack(Material.GLOWSTONE), new ItemStack(Material.GLOWSTONE_DUST, 4), 80));
		recipes.add(new CompressionRecipe("nether_warts_to_block", new ItemStack(Material.NETHER_WART_BLOCK), new ItemStack(Material.NETHER_WART, 9), 80));
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
		return "compressor";
	}


	@Override
	public String getDisplayName() {
		return "§eCompressor";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Compresses Materials to create Resources with higher density.", "§eSpeed: §6" + this.getSpeed());
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" S ", "PTP", "MCM");
		recipe.setIngredient('S', CustomItemSetup.STEEL_INGOT.createDefaultItemStack());
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('T', Material.IRON_TRAPDOOR);
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('C', CustomBlockSetup.MACHINE_CASING.createDefaultItemStack());
		return recipe;
	}

}
