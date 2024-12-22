
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.recipe.machine.RandomOutputsRecipe;



public class DyePress extends AbstractCraftingMachine {

	public DyePress() {
		super(MachineTier.IMPROVED);
	}


	@Override
	public List<MachineRecipe> getMachineRecipes(TileState block) {
		List<MachineRecipe> recipes = new ArrayList<>();

		recipes.add(new RandomOutputsRecipe(this, "dandelion", new ItemStack(Material.DANDELION), 60).addOutput(Material.YELLOW_DYE, 1.0, 1, 2).addOutput(Material.GREEN_DYE, 0.25, 1, 1).addOutput(Material.LIME_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "poppy", new ItemStack(Material.POPPY), 60).addOutput(Material.RED_DYE, 1.0, 1, 2).addOutput(Material.GREEN_DYE, 0.25, 1, 1).addOutput(Material.LIME_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "blue_orchid", new ItemStack(Material.BLUE_ORCHID), 60).addOutput(Material.LIGHT_BLUE_DYE, 1.0, 1, 2).addOutput(Material.GREEN_DYE, 0.25, 1, 1).addOutput(Material.LIME_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "allium", new ItemStack(Material.ALLIUM), 60).addOutput(Material.MAGENTA_DYE, 1.0, 1, 2).addOutput(Material.PINK_DYE, 0.5, 1, 1).addOutput(Material.GREEN_DYE, 0.25, 1, 1).addOutput(Material.LIME_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "azure_bluet", new ItemStack(Material.AZURE_BLUET), 60).addOutput(Material.LIGHT_GRAY_DYE, 1.0, 1, 2).addOutput(Material.WHITE_DYE, 0.25, 1, 1).addOutput(Material.GREEN_DYE, 0.25, 1, 1).addOutput(Material.LIME_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "red_tulip", new ItemStack(Material.RED_TULIP), 60).addOutput(Material.RED_DYE, 1.0, 1, 2).addOutput(Material.GREEN_DYE, 0.25, 1, 1).addOutput(Material.LIME_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "orange_tulip", new ItemStack(Material.ORANGE_TULIP), 60).addOutput(Material.ORANGE_DYE, 1.0, 1, 2).addOutput(Material.GREEN_DYE, 0.25, 1, 1).addOutput(Material.LIME_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "white_tulip", new ItemStack(Material.WHITE_TULIP), 60).addOutput(Material.WHITE_DYE, 1.0, 1, 2).addOutput(Material.GREEN_DYE, 0.25, 1, 1).addOutput(Material.LIME_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "pink_bluet", new ItemStack(Material.PINK_TULIP), 60).addOutput(Material.PINK_DYE, 1.0, 1, 2).addOutput(Material.GREEN_DYE, 0.25, 1, 1).addOutput(Material.LIME_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "oxeye_daisy", new ItemStack(Material.OXEYE_DAISY), 60).addOutput(Material.LIGHT_GRAY_DYE, 1.0, 1, 2).addOutput(Material.WHITE_DYE, 0.25, 1, 1).addOutput(Material.YELLOW_DYE, 0.125, 1, 1).addOutput(Material.GREEN_DYE, 0.25, 1, 1).addOutput(Material.LIME_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "cornflower", new ItemStack(Material.CORNFLOWER), 60).addOutput(Material.BLUE_DYE, 1.0, 1, 2).addOutput(Material.YELLOW_DYE, 0.125, 1, 1).addOutput(Material.GREEN_DYE, 0.25, 1, 1).addOutput(Material.LIME_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "lily_of_the_valley", new ItemStack(Material.LILY_OF_THE_VALLEY), 60).addOutput(Material.WHITE_DYE, 0.25, 1, 2).addOutput(Material.LIGHT_GRAY_DYE, 0.125, 1, 1).addOutput(Material.GREEN_DYE, 0.25, 1, 1).addOutput(Material.LIME_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "torchflower", new ItemStack(Material.TORCHFLOWER), 80).addOutput(Material.ORANGE_DYE, 1.0, 1, 2).addOutput(Material.YELLOW_DYE, 0.25, 1, 1).addOutput(Material.RED_DYE, 0.25, 1, 1).addOutput(Material.GREEN_DYE, 0.25, 1, 1).addOutput(Material.CYAN_DYE, 0.125, 1, 1).addOutput(Material.PURPLE_DYE, 0.125, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "whiter_rose", new ItemStack(Material.WITHER_ROSE), 120).addOutput(Material.BLACK_DYE, 1.0, 1, 2).addOutput(Material.GRAY_DYE, 0.5, 1, 2).addOutput(Material.GREEN_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "pink_petals", new ItemStack(Material.PINK_PETALS), 60).addOutput(Material.PINK_DYE, 1.0, 1, 2).addOutput(Material.MAGENTA_DYE, 0.5, 1, 1).addOutput(Material.LIME_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "spore_blossom", new ItemStack(Material.SPORE_BLOSSOM), 80).addOutput(Material.PINK_DYE, 1.0, 2, 6).addOutput(Material.MAGENTA_DYE, 0.5, 1, 3).addOutput(Material.GREEN_DYE, 0.25, 1, 2).addOutput(Material.LIME_DYE, 0.25, 1, 2));
		recipes.add(new RandomOutputsRecipe(this, "sunflower", new ItemStack(Material.SUNFLOWER), 100).addOutput(Material.YELLOW_DYE, 1.0, 2, 4).addOutput(Material.ORANGE_DYE, 0.25, 1, 2).addOutput(Material.GREEN_DYE, 0.25, 1, 3).addOutput(Material.LIME_DYE, 0.25, 1, 3));
		recipes.add(new RandomOutputsRecipe(this, "lilac", new ItemStack(Material.LILAC), 100).addOutput(Material.MAGENTA_DYE, 1.0, 2, 4).addOutput(Material.PINK_DYE, 0.5, 1, 2).addOutput(Material.GREEN_DYE, 0.25, 1, 2).addOutput(Material.LIME_DYE, 0.25, 1, 2));
		recipes.add(new RandomOutputsRecipe(this, "rose_bush", new ItemStack(Material.ROSE_BUSH), 100).addOutput(Material.RED_DYE, 1.0, 2, 4).addOutput(Material.GREEN_DYE, 0.25, 1, 2).addOutput(Material.LIME_DYE, 0.25, 1, 2));
		recipes.add(new RandomOutputsRecipe(this, "peony", new ItemStack(Material.PEONY), 100).addOutput(Material.PINK_DYE, 1.0, 2, 4).addOutput(Material.MAGENTA_DYE, 0.5, 1, 2).addOutput(Material.GREEN_DYE, 0.25, 1, 2).addOutput(Material.LIME_DYE, 0.25, 1, 2));
		recipes.add(new RandomOutputsRecipe(this, "pitcher_plant", new ItemStack(Material.PITCHER_PLANT), 100).addOutput(Material.CYAN_DYE, 1.0, 2, 4).addOutput(Material.PURPLE_DYE, 0.5, 1, 2).addOutput(Material.BLUE_DYE, 0.25, 1, 1).addOutput(Material.GREEN_DYE, 0.25, 1, 2).addOutput(Material.MAGENTA_DYE, 0.125, 1, 1).addOutput(Material.LIME_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "glow_berries", new ItemStack(Material.GLOW_BERRIES), 60).addOutput(Material.ORANGE_DYE, 0.5, 1, 1).addOutput(Material.YELLOW_DYE, 0.5, 1, 1).addOutput(Material.GLOWSTONE_DUST, 0.0625, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "cactus", new ItemStack(Material.CACTUS), 160).addOutput(Material.GREEN_DYE, 1.0, 3, 6));
		recipes.add(new RandomOutputsRecipe(this, "sea_pickle", new ItemStack(Material.SEA_PICKLE), 120).addOutput(Material.LIME_DYE, 1.0, 1, 3).addOutput(Material.GREEN_DYE, 0.5, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "beetroot", new ItemStack(Material.BEETROOT), 80).addOutput(Material.RED_DYE, 1.0, 2, 5));
		recipes.add(new RandomOutputsRecipe(this, "cocoa_beans", new ItemStack(Material.COCOA_BEANS), 80).addOutput(Material.BROWN_DYE, 1.0, 2, 3));
		recipes.add(new RandomOutputsRecipe(this, "bone_meal", new ItemStack(Material.BONE_MEAL), 120).addOutput(Material.WHITE_DYE, 1.0, 1, 2).addOutput(Material.LIGHT_GRAY_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "bone_meal", new ItemStack(Material.LAPIS_LAZULI), 120).addOutput(Material.BLUE_DYE, 1.0, 1, 2).addOutput(Material.LIGHT_BLUE_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "ink_sac", new ItemStack(Material.INK_SAC), 80).addOutput(Material.BLACK_DYE, 1.0, 1, 3));
		recipes.add(new RandomOutputsRecipe(this, "glow_ink_saw", new ItemStack(Material.GLOW_INK_SAC), 80).addOutput(Material.BLACK_DYE, 0.75, 1, 2).addOutput(Material.CYAN_DYE, 0.75, 1, 2).addOutput(Material.GLOWSTONE_DUST, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "chorus_fruit", new ItemStack(Material.CHORUS_FRUIT), 120).addOutput(Material.PURPLE_DYE, 1.0, 1, 2).addOutput(Material.MAGENTA_DYE, 0.5, 1, 1).addOutput(Material.PINK_DYE, 0.25, 1, 1));
		recipes.add(new RandomOutputsRecipe(this, "popped_chorus_fruit", new ItemStack(Material.POPPED_CHORUS_FRUIT), 120).addOutput(Material.PURPLE_DYE, 1.0, 1, 3).addOutput(Material.MAGENTA_DYE, 0.25, 1, 1));

		return recipes;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.STICKY_PISTON;
	}


	@Override
	public String getTitle() {
		return "§fDye Press";
	}


	@Override
	public String getMachineRegistryName() {
		return "dye_press";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.DYE_PRESS;
	}

}
