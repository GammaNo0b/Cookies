
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.recipe.machine.RandomOutputsRecipe;



public class MineralExtractor extends AbstractCraftingMachine {

	public MineralExtractor(MachineTier tier) {
		super(tier);
	}


	@Override
	public List<MachineRecipe> getMachineRecipes(TileState block) {
		List<MachineRecipe> recipes = new ArrayList<>();

		if(this.tier.getTier() > 0) {
			recipes.add(new RandomOutputsRecipe(this, "coarse_dirt_to_dirt", new ItemStack(Material.COARSE_DIRT), 100).addOutput(Material.DIRT, 0.8, 1, 1).addOutput(Material.FLINT, 0.5, 1, 2));
			recipes.add(new RandomOutputsRecipe(this, "roouted_dirt_to_dirt", new ItemStack(Material.ROOTED_DIRT), 100).addOutput(Material.DIRT, 0.8, 1, 1).addOutput(Material.HANGING_ROOTS, 0.5, 1, 1));

			if(this.tier.getTier() > 1) {
				recipes.add(new RandomOutputsRecipe(this, "sand_to_dusts", new ItemStack(Material.SAND), 100).addOutput(Items.DUST, 0.8, 1, 1).addOutput(Items.QUARTZ_DUST, 0.05, 1, 2));
				recipes.add(new RandomOutputsRecipe(this, "red_sand_to_dusts", new ItemStack(Material.RED_SAND), 100).addOutput(Items.RED_DUST, 0.8, 1, 1).addOutput(Material.GOLD_NUGGET, 0.1, 1, 5).addOutput(Material.RAW_GOLD, 0.01, 1, 1));
				recipes.add(new RandomOutputsRecipe(this, "dust_to_resources", Items.DUST.get(), 80).addOutput(Items.QUARTZ_DUST, 0.025, 1, 2).addOutput(Items.SILICON, 0.025, 1, 2));
				recipes.add(new RandomOutputsRecipe(this, "red_dust_to_resources", Items.RED_DUST.get(), 80).addOutput(Items.GOLD_DUST, 0.05, 1, 3).addOutput(Items.QUARTZ_DUST, 0.02, 1, 2).addOutput(Items.SILICON, 0.02, 1, 1));
				recipes.add(new RandomOutputsRecipe(this, "gravel_to_dusts", new ItemStack(Material.GRAVEL), 200).addOutput(Items.IRON_DUST.get(), 0.025, 1, 3).addOutput(Items.GOLD_DUST.get(), 0.01, 1, 2).addOutput(Items.COPPER_DUST.get(), 0.025, 1, 4).addOutput(Items.ALUMINUM_DUST.get(), 0.02, 1, 3).addOutput(Items.TIN_DUST, 0.02, 1, 2).addOutput(Items.NICKEL_DUST.get(), 0.01, 1, 1).addOutput(Items.LEAD_DUST.get(), 0.005, 1, 1).addOutput(Items.SILVER_DUST.get(), 0.005, 1, 1).addOutput(Items.LITHIUM_DUST.get(), 0.0025, 1, 1).addOutput(Items.MAGNESIUM_DUST, 0.001, 1, 1));

				if(this.tier.getTier() > 2) {
					recipes.add(new RandomOutputsRecipe(this, "soulsand_to_resources", new ItemStack(Material.SOUL_SAND), 200).addOutput(Material.BLAZE_POWDER, 0.01, 1, 2).addOutput(Material.GHAST_TEAR, 0.005, 1, 1).addOutput(Material.NETHER_WART, 0.02, 1, 2).addOutput(Material.QUARTZ, 0.01, 1, 3).addOutput(Material.GOLD_NUGGET, 0.01, 2, 6));
					recipes.add(new RandomOutputsRecipe(this, "soulsoil_to_resources", new ItemStack(Material.SOUL_SOIL), 200).addOutput(Material.GHAST_TEAR, 0.001, 1, 1).addOutput(Material.BONE, 0.01, 1, 2).addOutput(Material.ENDER_PEARL, 0.005, 1, 1));

					if(this.tier.getTier() > 3) {
						recipes.add(new RandomOutputsRecipe(this, "crushed_netherrack_to_resources", Items.CRUSHED_NETHERRACK.get(), 100).addOutput(Items.SOUL_DUST.get(), 0.005, 1, 3).addOutput(Material.BLAZE_POWDER, 0.001, 1, 1).addOutput(Items.SULFUR.get(), 0.001, 1, 1).addOutput(Material.GOLD_NUGGET, 0.002, 1, 3).addOutput(Material.QUARTZ, 0.002, 1, 2).addOutput(Material.ANCIENT_DEBRIS, 0.0001, 1, 1));
						recipes.add(new RandomOutputsRecipe(this, "crushed_end_stone_to_resources", Items.CRUSHED_END_STONE.get(), 200).addOutput(Material.CHORUS_FRUIT, 0.005, 1, 2).addOutput(Material.CHORUS_FLOWER, 0.001, 1, 1).addOutput(Material.ENDER_PEARL, 0.001, 1, 1).addOutput(Material.SHULKER_SHELL, 0.0001, 1, 1));
						recipes.add(new RandomOutputsRecipe(this, "crushed_obsidian_to_resources", Items.CRUSHED_OBSIDIAN.get(), 400).addOutput(Material.COAL, 0.01, 1, 3).addOutput(Material.REDSTONE, 0.0025, 1, 4).addOutput(Material.LAPIS_LAZULI, 0.0025, 1, 4).addOutput(Material.AMETHYST_SHARD, 0.001, 1, 2).addOutput(Material.EMERALD, 0.001, 1, 2).addOutput(Material.DIAMOND, 0.001, 1, 2));
						recipes.add(new RandomOutputsRecipe(this, "crushed_crying_obsidian_to_resources", Items.CRUSHED_CRYING_OBSIDIAN.get(), 400).addOutput(Items.CRUSHED_OBSIDIAN.get(), 0.5, 1, 1).addOutput(Material.COAL, 0.01, 1, 4).addOutput(Material.REDSTONE, 0.0025, 2, 7).addOutput(Material.LAPIS_LAZULI, 0.0025, 2, 7).addOutput(Material.AMETHYST_SHARD, 0.001, 1, 4).addOutput(Material.EMERALD, 0.001, 1, 3).addOutput(Material.DIAMOND, 0.001, 1, 3));
					}
				}
			}
		}

		return recipes;
	}


	@Override
	public Material getProgressMaterial(double progress) {
		switch (this.tier) {
			case BASIC:
				return Material.IRON_SHOVEL;
			case ADVANCED:
				return Material.GOLDEN_SHOVEL;
			case IMPROVED:
				return Material.DIAMOND_SHOVEL;
			case PERFECTED:
				return Material.NETHERITE_SHOVEL;
			default:
				return null;
		}
	}


	@Override
	public String getMachineRegistryName() {
		return "mineral_extractor";
	}


	@Override
	public String getTitle() {
		return this.tier.getName() + " Mineral Extractor";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.MINERAL_EXTRACTOR;
	}

}
