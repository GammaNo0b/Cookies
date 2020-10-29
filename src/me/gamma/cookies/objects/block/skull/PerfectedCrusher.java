
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



public class PerfectedCrusher extends PerfectedMachine {

	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>(CustomBlockSetup.IMPROVED_CRUSHER.getMachineRecipes());

		return recipes;
	}


	@Override
	public ItemStack getProgressIcon() {
		return new ItemStack(Material.NETHERITE_PICKAXE);
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CRUSHER;
	}


	@Override
	public String getIdentifier() {
		return "perfected_crusher";
	}


	@Override
	public String getDisplayName() {
		return "§8Perfected Crusher";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Crushes materials into more dustier ones.", "§eSpeed: §6" + this.getSpeed());
	}


	@Override
	public int getSpeed() {
		return 20;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" R ", "PCP", "MSM");
		recipe.setIngredient('R', CustomBlockSetup.IMPROVED_CRUSHER.createDefaultItemStack());
		recipe.setIngredient('P', Material.NETHERITE_PICKAXE);
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('S', Material.NETHERITE_INGOT);
		recipe.setIngredient('C', CustomBlockSetup.PERFECTED_MACHINE_CASING.createDefaultItemStack());
		return recipe;
	}

}
