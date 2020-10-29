
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



public class Crusher extends BasicMachine {

	@Override
	public ItemStack getProgressIcon() {
		return new ItemStack(Material.IRON_PICKAXE);
	}


	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>();
		recipes.add(new SimpleMachineRecipe("wool_to_string", new ItemStack(Material.STRING, 4), new ItemStack(Material.WHITE_WOOL), 80));
		recipes.add(new SimpleMachineRecipe("melon_into_slices", new ItemStack(Material.MELON_SLICE, 9), new ItemStack(Material.MELON), 120));
		recipes.add(new SimpleMachineRecipe("glowstone_into_dust", new ItemStack(Material.GLOWSTONE_DUST, 4), new ItemStack(Material.GLOWSTONE), 120));
		recipes.add(new SimpleMachineRecipe("stone_to_cobble", new ItemStack(Material.COBBLESTONE), new ItemStack(Material.STONE), 200));
		recipes.add(new SimpleMachineRecipe("cobble_to_gravel", new ItemStack(Material.GRAVEL), new ItemStack(Material.COBBLESTONE), 160));
		recipes.add(new SimpleMachineRecipe("gravel_to_sand", new ItemStack(Material.SAND), new ItemStack(Material.GRAVEL), 120));
		return recipes;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CRUSHER;
	}


	@Override
	public String getIdentifier() {
		return "crusher";
	}


	@Override
	public String getDisplayName() {
		return "§8Crusher";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Crushes various Materials to more dustier ones.", "§eSpeed: §6" + this.getSpeed());
	}


	@Override
	public int getSpeed() {
		return 1;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" H ", "PCP", "MAM");
		recipe.setIngredient('H', Material.HOPPER);
		recipe.setIngredient('P', Material.IRON_PICKAXE);
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('A', CustomItemSetup.ALUMINUM_INGOT.createDefaultItemStack());
		recipe.setIngredient('C', CustomBlockSetup.MACHINE_CASING.createDefaultItemStack());
		return recipe;
	}

}
