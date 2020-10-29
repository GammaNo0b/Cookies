
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



public class Smeltery extends BasicMachine {

	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>();
		recipes.add(new AlloyRecipe("iron_dust_to_iron_ingot", new ItemStack(Material.IRON_INGOT), 120, CustomItemSetup.IRON_DUST.createDefaultItemStack()));
		recipes.add(new AlloyRecipe("gold_dust_to_gold_ingot", new ItemStack(Material.GOLD_INGOT), 120, CustomItemSetup.GOLD_DUST.createDefaultItemStack()));
		recipes.add(new AlloyRecipe("copper_dust_to_copper_ingot", CustomItemSetup.COPPER_INGOT.createDefaultItemStack(), 120, CustomItemSetup.COPPER_DUST.createDefaultItemStack()));
		recipes.add(new AlloyRecipe("aluminum_dust_to_aluminum_ingot", CustomItemSetup.ALUMINUM_INGOT.createDefaultItemStack(), 120, CustomItemSetup.ALUMINUM_DUST.createDefaultItemStack()));
		recipes.add(new AlloyRecipe("lead_dust_to_lead_ingot", CustomItemSetup.LEAD_INGOT.createDefaultItemStack(), 120, CustomItemSetup.LEAD_DUST.createDefaultItemStack()));
		recipes.add(new AlloyRecipe("silver_dust_to_silver_ingot", CustomItemSetup.SILVER_INGOT.createDefaultItemStack(), 120, CustomItemSetup.SILVER_DUST.createDefaultItemStack()));
		recipes.add(new AlloyRecipe("steel_dust_to_steel_ingot", CustomItemSetup.STEEL_INGOT.createDefaultItemStack(), 120, CustomItemSetup.STEEL_DUST.createDefaultItemStack()));
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
		return "smeltery";
	}


	@Override
	public String getDisplayName() {
		return "§cSmeltery";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Smelts and Alloys different Materials into Ingots.", "§eSpeed: §6" + this.getSpeed());
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" S ", "HTH", "SCS");
		recipe.setIngredient('H', CustomBlockSetup.COPPER_COIL.createDefaultItemStack());
		recipe.setIngredient('S', CustomItemSetup.STEEL_INGOT.createDefaultItemStack());
		recipe.setIngredient('C', CustomBlockSetup.MACHINE_CASING.createDefaultItemStack());
		recipe.setIngredient('T', Material.IRON_TRAPDOOR);
		return recipe;
	}

}
