
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.ListProperty;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.recipe.machine.RandomOutputsRecipe;
import me.gamma.cookies.object.tile.machine.AerialExtractor;



public class AerialExtractorBlock extends AbstractCraftingMachineBlock<AerialExtractorBlock, AerialExtractor> {

	public static final ListProperty<ItemStack, ItemStackProperty> RESULTS = new ListProperty<>("results", ItemStackProperty::new);

	public AerialExtractorBlock(MachineTier tier) {
		super(tier);
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


	@Override
	public AerialExtractorBlock castCustomBlock() {
		return this;
	}


	@Override
	public AerialExtractor createNewTileEntity(Block block) {
		return new AerialExtractor(this, block);
	}


	private final List<MachineRecipe> getOverworldRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>();
		RandomOutputsRecipe recipe = new RandomOutputsRecipe(this, "recipe", null, 200);
		recipe.addOutput(Material.FEATHER, 0.01D, 1, 1);
		recipe.addOutput(Material.STRING, 0.01D, 1, 1);
		recipe.addOutput(Material.BONE_MEAL, 0.001D, 1, 1);
		recipe.addOutput(Material.GUNPOWDER, 0.0025D, 1, 1);
		recipe.addOutput(Material.SNOWBALL, 0.01D, 1, 1);
		recipe.addOutput(Material.PAPER, 0.02D, 1, 1);
		recipe.addOutput(Items.PULVERIZED_COAL, 0.001D, 1, 1);
		recipes.add(recipe);
		return recipes;
	}


	private final List<MachineRecipe> getNetherRecipes(boolean soulsandBiome) {
		List<MachineRecipe> recipes = new ArrayList<>();
		RandomOutputsRecipe recipe = new RandomOutputsRecipe(this, "recipe", null, 300);
		recipe.addOutput(Items.QUARTZ_DUST, 0.002D, 1, 1);
		recipe.addOutput(Items.GOLD_DUST, 0.0025D, 1, 1);
		recipe.addOutput(Material.BLAZE_POWDER, 0.002D, 1, 1);
		recipe.addOutput(Material.GLOWSTONE_DUST, 0.005D, 1, 1);
		recipe.addOutput(Items.SULFUR, 0.002D, 1, 1);
		recipe.addOutput(Items.SOUL_DUST, soulsandBiome ? 0.01D : 0.001D, 1, 1);
		recipe.addOutput(Material.GHAST_TEAR, 0.0001D, 1, 1);
		recipes.add(recipe);
		return recipes;
	}


	private final List<MachineRecipe> getEndRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>();
		RandomOutputsRecipe recipe = new RandomOutputsRecipe(this, "recipe", null, 400);
		recipe.addOutput(Material.ENDER_PEARL, 0.001D, 1, 1);
		recipe.addOutput(Material.ENDER_EYE, 0.0001D, 1, 1);
		recipes.add(recipe);
		return recipes;
	}


	@Override
	public List<MachineRecipe> getMachineRecipes(Block block) {
		return switch (block.getWorld().getEnvironment()) {
			case NORMAL -> this.getOverworldRecipes();
			case NETHER -> this.getNetherRecipes(block.getBiome().getKeyOrNull() == Biome.SOUL_SAND_VALLEY.getKeyOrNull());
			case THE_END -> this.getEndRecipes();
			default -> new ArrayList<>();
		};
	}


	@Override
	public List<MachineRecipe> getAllMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>();
		recipes.addAll(this.getOverworldRecipes());
		recipes.addAll(this.getNetherRecipes(false));
		recipes.addAll(this.getEndRecipes());
		return recipes;
	}

}
