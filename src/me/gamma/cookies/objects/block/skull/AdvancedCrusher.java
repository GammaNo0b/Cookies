
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
import me.gamma.cookies.objects.recipe.SimpleMachineRecipe;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;



public class AdvancedCrusher extends AdvancedMachine {

	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>(CustomBlockSetup.CRUSHER.getMachineRecipes());
		recipes.add(new SimpleMachineRecipe("coal_to_dust", CustomItemSetup.COAL_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, new ItemStack(Material.COAL), 160));
		recipes.add(new SimpleMachineRecipe("iron_ingot_to_dust", CustomItemSetup.IRON_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, new ItemStack(Material.IRON_INGOT), 160));
		recipes.add(new SimpleMachineRecipe("gold_ingot_to_dust", CustomItemSetup.GOLD_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, new ItemStack(Material.GOLD_INGOT), 160));
		recipes.add(new SimpleMachineRecipe("copper_ingot_to_dust", CustomItemSetup.COPPER_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, CustomItemSetup.COPPER_INGOT.createDefaultItemStack(), 160));
		recipes.add(new SimpleMachineRecipe("aluminum_ingot_to_dust", CustomItemSetup.ALUMINUM_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, CustomItemSetup.ALUMINUM_INGOT.createDefaultItemStack(), 160));
		recipes.add(new SimpleMachineRecipe("lead_ingot_to_dust", CustomItemSetup.LEAD_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, CustomItemSetup.LEAD_INGOT.createDefaultItemStack(), 160));
		recipes.add(new SimpleMachineRecipe("silver_ingot_to_dust", CustomItemSetup.SILVER_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, CustomItemSetup.SILVER_INGOT.createDefaultItemStack(), 160));
		recipes.add(new SimpleMachineRecipe("steel_ingot_to_dust", CustomItemSetup.STEEL_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, CustomItemSetup.STEEL_INGOT.createDefaultItemStack(), 160));
		return recipes;
	}


	@Override
	public ItemStack getProgressIcon() {
		return new ItemStack(Material.GOLDEN_PICKAXE);
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CRUSHER;
	}


	@Override
	public String getIdentifier() {
		return "advanced_crusher";
	}


	@Override
	public String getDisplayName() {
		return "§8Advanced Crusher";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Crushes resources out of ores.", "§eSpeed: §6" + this.getSpeed());
	}


	@Override
	public int getSpeed() {
		return 4;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" R ", "PCP", "MIM");
		recipe.setIngredient('R', CustomBlockSetup.CRUSHER.createDefaultItemStack());
		recipe.setIngredient('P', Material.GOLDEN_PICKAXE);
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('C', CustomBlockSetup.ADVANCED_MACHINE_CASING.createDefaultItemStack());
		return recipe;
	}

}
