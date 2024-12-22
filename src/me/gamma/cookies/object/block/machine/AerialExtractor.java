
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.ListProperty;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.recipe.machine.RandomOutputsRecipe;



public class AerialExtractor extends AbstractCraftingMachine {

	public static final ListProperty<ItemStack, ItemStackProperty> RESULTS = new ListProperty<>("results", ItemStackProperty::new);

	public AerialExtractor(MachineTier tier) {
		super(tier);
	}


	@Override
	public List<MachineRecipe> getMachineRecipes(TileState block) {
		List<MachineRecipe> recipes = new ArrayList<>();
		Environment environment = block == null ? null : block.getWorld().getEnvironment();

		if(environment == null || environment == Environment.NORMAL) {
			RandomOutputsRecipe recipe = new RandomOutputsRecipe(this, "recipe", null, 200);
			recipe.addOutput(Material.FEATHER, 0.01D, 1, 1);
			recipe.addOutput(Material.STRING, 0.01D, 1, 1);
			recipe.addOutput(Material.BONE_MEAL, 0.001D, 1, 1);
			recipe.addOutput(Material.GUNPOWDER, 0.0025D, 1, 1);
			recipe.addOutput(Material.SNOWBALL, 0.01D, 1, 1);
			recipe.addOutput(Material.PAPER, 0.02D, 1, 1);
			recipe.addOutput(Items.PULVERIZED_COAL, 0.001D, 1, 1);
			recipes.add(recipe);
		}

		if(environment == null || environment == Environment.NETHER) {
			RandomOutputsRecipe recipe = new RandomOutputsRecipe(this, "recipe", null, 300);
			recipe.addOutput(Items.QUARTZ_DUST, 0.002D, 1, 1);
			recipe.addOutput(Items.GOLD_DUST, 0.0025D, 1, 1);
			recipe.addOutput(Material.BLAZE_POWDER, 0.002D, 1, 1);
			recipe.addOutput(Material.GLOWSTONE_DUST, 0.005D, 1, 1);
			recipe.addOutput(Items.SULFUR, 0.002D, 1, 1);
			recipe.addOutput(Items.SOUL_DUST, block == null || block.getWorld().getBiome(block.getLocation()) == Biome.SOUL_SAND_VALLEY ? 0.01D : 0.001D, 1, 1);
			recipe.addOutput(Material.GHAST_TEAR, 0.0001D, 1, 1);
			recipes.add(recipe);
		}

		if(environment == null || environment == Environment.THE_END) {
			RandomOutputsRecipe recipe = new RandomOutputsRecipe(this, "recipe", null, 400);
			recipe.addOutput(Material.ENDER_PEARL, 0.001D, 1, 1);
			recipe.addOutput(Material.ENDER_EYE, 0.0001D, 1, 1);
			recipes.add(recipe);
		}

		return recipes;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.IRON_BARS;
	}


	@Override
	public String getTitle() {
		return this.tier.getName() + " Aerial Extractor";
	}


	@Override
	public String getMachineRegistryName() {
		return "aerial_extractor";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.AERIAL_EXTRACTOR;
	}

}
