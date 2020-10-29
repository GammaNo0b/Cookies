package me.gamma.cookies.objects.block.skull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.AlloyRecipe;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.MachineRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;


public class AdvancedSmeltery extends AdvancedMachine {

	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>(CustomBlockSetup.SMELTERY.getMachineRecipes());
		recipes.add(new AlloyRecipe("iron_carbon_to_steal", CustomItemSetup.STEEL_INGOT.createDefaultItemStack(), 200, new ItemStack(Material.IRON_INGOT), CustomItemSetup.CARBON.createDefaultItemStack()));
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
		return "advanced_smeltery";
	}


	@Override
	public String getDisplayName() {
		return "§cAdvanced Smeltery";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Has two Slots which allows basic alloying.", "§eSpeed: §6" + this.getSpeed());
	}
	
	
	@Override
	public int getSpeed() {
		return 4;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" S ", "HBH", "SCS");
		recipe.setIngredient('H', CustomBlockSetup.COPPER_COIL.createDefaultItemStack());
		recipe.setIngredient('S', CustomItemSetup.STEEL_INGOT.createDefaultItemStack());
		recipe.setIngredient('C', CustomBlockSetup.ADVANCED_MACHINE_CASING.createDefaultItemStack());
		recipe.setIngredient('B', CustomBlockSetup.SMELTERY.createDefaultItemStack());
		return recipe;
	}

}
