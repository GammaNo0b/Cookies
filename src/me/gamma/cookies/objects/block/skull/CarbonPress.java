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


public class CarbonPress extends BasicMachine {

	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>();
		recipes.add(new CompressionRecipe("coal_to_compressed_coal", CustomItemSetup.CARBON.createDefaultItemStack(), new ItemStack(Material.COAL, 4), 120));
		recipes.add(new CompressionRecipe("carbon_to_compressed_carbon", CustomItemSetup.COMPRESSED_CARBON.createDefaultItemStack(), CustomItemSetup.CARBON.createDefaultItemStack(), 8, 160));
		recipes.add(new CompressionRecipe("compressed_carbon_to_carbon_chunk", CustomItemSetup.CARBON_CHUNK.createDefaultItemStack(), CustomItemSetup.COMPRESSED_CARBON.createDefaultItemStack(), 8, 200));
		recipes.add(new CompressionRecipe("carbon_chunk_to_carbonado", CustomItemSetup.CARBONADO.createDefaultItemStack(), CustomItemSetup.CARBON_CHUNK.createDefaultItemStack(), 4, 240));
		recipes.add(new CompressionRecipe("carbonado_to_diamond", new ItemStack(Material.DIAMOND), CustomItemSetup.CARBONADO.createDefaultItemStack(), 320));
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
		return "carbon_press";
	}


	@Override
	public String getDisplayName() {
		return "§8Carbon Press";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Special Compression Machine that only compresses carbon related resources.", "§eSpeed: §6" + this.getSpeed());
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" S ", "MTM", "KCK");
		recipe.setIngredient('S', CustomItemSetup.STEEL_INGOT.createDefaultItemStack());
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('T', Material.IRON_TRAPDOOR);
		recipe.setIngredient('C', CustomBlockSetup.MACHINE_CASING.createDefaultItemStack());
		recipe.setIngredient('K', CustomItemSetup.CARBON.createDefaultItemStack());
		return recipe;
	}

}
