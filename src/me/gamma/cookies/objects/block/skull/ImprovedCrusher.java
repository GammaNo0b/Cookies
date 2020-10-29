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


public class ImprovedCrusher extends ImprovedMachine {

	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>(CustomBlockSetup.ADVANCED_CRUSHER.getMachineRecipes());
		recipes.add(new SimpleMachineRecipe("coal_ore_to_coal", new ItemStack(Material.COAL, 2), new ItemStack(Material.COAL_ORE), 160));
		recipes.add(new SimpleMachineRecipe("iron_ore_to_iron_dust", new ItemStack(Material.IRON_INGOT, 2), new ItemStack(Material.IRON_ORE), 160));
		recipes.add(new SimpleMachineRecipe("gold_ore_to_gold_dust", new ItemStack(Material.GOLD_INGOT, 2), new ItemStack(Material.GOLD_ORE), 160));
		recipes.add(new SimpleMachineRecipe("redstone_ore_to_redstone", new ItemStack(Material.REDSTONE, 12), new ItemStack(Material.REDSTONE_ORE), 160));
		recipes.add(new SimpleMachineRecipe("lapis_ore_to_lapis_lazuli", new ItemStack(Material.LAPIS_LAZULI, 10), new ItemStack(Material.LAPIS_ORE), 160));
		recipes.add(new SimpleMachineRecipe("emerald_ore_to_emeralds", new ItemStack(Material.EMERALD, 2), new ItemStack(Material.EMERALD_ORE), 160));
		recipes.add(new SimpleMachineRecipe("diamond_ore_to_diamonds", new ItemStack(Material.DIAMOND, 2), new ItemStack(Material.DIAMOND_ORE), 160));
		recipes.add(new SimpleMachineRecipe("nether_gold_ore_to_gold_nuggets", new ItemStack(Material.GOLD_NUGGET, 6), new ItemStack(Material.NETHER_GOLD_ORE), 160));
		recipes.add(new SimpleMachineRecipe("nether_quartz_ore_to_quartz", new ItemStack(Material.QUARTZ, 3), new ItemStack(Material.NETHER_QUARTZ_ORE), 160));
		recipes.add(new SimpleMachineRecipe("ancient_debris_to_netherite_scrap", new ItemStack(Material.NETHERITE_SCRAP, 2), new ItemStack(Material.ANCIENT_DEBRIS), 160));
		return recipes;
	}


	@Override
	public ItemStack getProgressIcon() {
		return new ItemStack(Material.DIAMOND_PICKAXE);
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CRUSHER;
	}


	@Override
	public String getIdentifier() {
		return "improved_crusher";
	}


	@Override
	public String getDisplayName() {
		return "§8Improved Crusher";
	}
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Crushes materials into more dustier ones.", "§eSpeed: §6" + this.getSpeed());
	}
	
	
	@Override
	public int getSpeed() {
		return 10;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" R ", "PCP", "MSM");
		recipe.setIngredient('R', CustomBlockSetup.ADVANCED_CRUSHER.createDefaultItemStack());
		recipe.setIngredient('P', Material.DIAMOND_PICKAXE);
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('S', CustomItemSetup.STEEL_INGOT.createDefaultItemStack());
		recipe.setIngredient('C', CustomBlockSetup.IMPROVED_MACHINE_CASING.createDefaultItemStack());
		return recipe;
	}

}
