
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.recipe.machine.SimpleMachineRecipe;



public class Compressor extends AbstractCraftingMachine {

	public Compressor(MachineTier tier) {
		super(tier);
	}


	@Override
	public List<MachineRecipe> getMachineRecipes(TileState block) {
		List<MachineRecipe> recipes = new ArrayList<>();

		if(this.tier.getTier() > 0) {
			recipes.add(new SimpleMachineRecipe(this, "wheat_to_hay_block", new ItemStack(Material.HAY_BLOCK), new ItemStack(Material.WHEAT, 9), 80));
			recipes.add(new SimpleMachineRecipe(this, "melons_to_block", new ItemStack(Material.MELON), new ItemStack(Material.MELON_SLICE, 9), 100));
			recipes.add(new SimpleMachineRecipe(this, "bamboo_to_block", new ItemStack(Material.BAMBOO_BLOCK), new ItemStack(Material.BAMBOO, 9), 100));
			recipes.add(new SimpleMachineRecipe(this, "dried_kelp_to_block", new ItemStack(Material.DRIED_KELP_BLOCK), new ItemStack(Material.DRIED_KELP, 9), 80));
			recipes.add(new SimpleMachineRecipe(this, "clay_balls_to_clay_block", new ItemStack(Material.CLAY), new ItemStack(Material.CLAY_BALL, 4), 80));
			recipes.add(new SimpleMachineRecipe(this, "snow_balls_to_snow_block", new ItemStack(Material.SNOW_BLOCK), new ItemStack(Material.SNOWBALL, 4), 80));
			recipes.add(new SimpleMachineRecipe(this, "dripstone_to_block", new ItemStack(Material.DRIPSTONE_BLOCK), new ItemStack(Material.POINTED_DRIPSTONE, 4), 160));
			recipes.add(new SimpleMachineRecipe(this, "string_to_wool", new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.STRING, 4), 100));
			recipes.add(new SimpleMachineRecipe(this, "bone_meal_to_bone_block", new ItemStack(Material.BONE_BLOCK), new ItemStack(Material.BONE_MEAL, 9), 160));
			recipes.add(new SimpleMachineRecipe(this, "glowstone_to_block", new ItemStack(Material.GLOWSTONE), new ItemStack(Material.GLOWSTONE_DUST, 4), 120));
			recipes.add(new SimpleMachineRecipe(this, "magma_cream_to_block", new ItemStack(Material.MAGMA_BLOCK), new ItemStack(Material.MAGMA_CREAM, 4), 160));
			recipes.add(new SimpleMachineRecipe(this, "nether_warts_to_block", new ItemStack(Material.NETHER_WART_BLOCK), new ItemStack(Material.NETHER_WART, 9), 100));
			recipes.add(new SimpleMachineRecipe(this, "compressed_cobblestone", Items.COMPRESSED_COBBLESTONE.get(), new ItemStack(Material.COBBLESTONE, 9), 200));
			recipes.add(new SimpleMachineRecipe(this, "double_compressed_cobblestone", Items.DOUBLE_COMPRESSED_COBBLESTONE.get(), Items.COMPRESSED_COBBLESTONE.get(), 9, 400));

			if(this.tier.getTier() > 1) {
				recipes.add(new SimpleMachineRecipe(this, "coal_to_coal_block", new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL, 9), 200));
				recipes.add(new SimpleMachineRecipe(this, "iron_nugget_to_iron_ingot", new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_NUGGET, 9), 200));
				recipes.add(new SimpleMachineRecipe(this, "iron_to_iron_block", new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_INGOT, 9), 200));
				recipes.add(new SimpleMachineRecipe(this, "copper_to_copper_block", new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_INGOT, 9), 200));
				recipes.add(new SimpleMachineRecipe(this, "gold_nugget_to_gold_ingot", new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.GOLD_NUGGET, 9), 200));
				recipes.add(new SimpleMachineRecipe(this, "gold_to_gold_block", new ItemStack(Material.GOLD_BLOCK), new ItemStack(Material.GOLD_INGOT, 9), 200));
				recipes.add(new SimpleMachineRecipe(this, "redstone_to_redstone_block", new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.REDSTONE, 9), 200));
				recipes.add(new SimpleMachineRecipe(this, "lapis_to_lapis_block", new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_LAZULI, 9), 200));
				recipes.add(new SimpleMachineRecipe(this, "emerald_to_emerald_block", new ItemStack(Material.EMERALD_BLOCK), new ItemStack(Material.EMERALD, 9), 200));
				recipes.add(new SimpleMachineRecipe(this, "diamond_to_diamond_block", new ItemStack(Material.DIAMOND_BLOCK), new ItemStack(Material.DIAMOND, 9), 200));
				recipes.add(new SimpleMachineRecipe(this, "quartz_to_quartz_block", new ItemStack(Material.QUARTZ_BLOCK), new ItemStack(Material.QUARTZ, 4), 200));
				recipes.add(new SimpleMachineRecipe(this, "amethyst_shards_to_amethyst_block", new ItemStack(Material.AMETHYST_BLOCK), new ItemStack(Material.AMETHYST_SHARD, 4), 200));
				recipes.add(new SimpleMachineRecipe(this, "netherite_to_netherite_block", new ItemStack(Material.NETHERITE_BLOCK), new ItemStack(Material.NETHERITE_INGOT, 9), 200));
				recipes.add(new SimpleMachineRecipe(this, "raw_iron_to_raw_iron_block", new ItemStack(Material.RAW_IRON_BLOCK), new ItemStack(Material.RAW_IRON, 9), 200));
				recipes.add(new SimpleMachineRecipe(this, "raw_copper_to_raw_copper_block", new ItemStack(Material.RAW_COPPER_BLOCK), new ItemStack(Material.RAW_COPPER, 9), 200));
				recipes.add(new SimpleMachineRecipe(this, "raw_gold_to_raw_gold_block", new ItemStack(Material.RAW_GOLD_BLOCK), new ItemStack(Material.RAW_GOLD, 9), 200));
				recipes.add(new SimpleMachineRecipe(this, "triple_compressed_cobblestone", Items.TRIPLE_COMPRESSED_COBBLESTONE.get(), Items.DOUBLE_COMPRESSED_COBBLESTONE.get(), 9, 600));
				recipes.add(new SimpleMachineRecipe(this, "quadruple_compressed_cobblestone", Items.QUADRUPLE_COMPRESSED_COBBLESTONE.get(), Items.TRIPLE_COMPRESSED_COBBLESTONE.get(), 9, 1200));

				if(this.tier.getTier() > 2) {
					recipes.add(new SimpleMachineRecipe(this, "soul_dust_to_soul_sand", new ItemStack(Material.SOUL_SAND), Items.SOUL_DUST.get(), 4, 200));
					recipes.add(new SimpleMachineRecipe(this, "quintuple_compressed_cobblestone", Items.QUINTUPLE_COMPRESSED_COBBLESTONE.get(), Items.QUADRUPLE_COMPRESSED_COBBLESTONE.get(), 9, 2400));
					recipes.add(new SimpleMachineRecipe(this, "sextuple_compressed_cobblestone", Items.SEXTUPLE_COMPRESSED_COBBLESTONE.get(), Items.QUINTUPLE_COMPRESSED_COBBLESTONE.get(), 9, 6000));

					if(this.tier.getTier() > 3) {
						recipes.add(new SimpleMachineRecipe(this, "septuple_compressed_cobblestone", Items.SEPTUPLE_COMPRESSED_COBBLESTONE.get(), Items.SEXTUPLE_COMPRESSED_COBBLESTONE.get(), 9, 12000));
						recipes.add(new SimpleMachineRecipe(this, "octuple_compressed_cobblestone", Items.OCTUPLE_COMPRESSED_COBBLESTONE.get(), Items.SEPTUPLE_COMPRESSED_COBBLESTONE.get(), 9, 24000));
						recipes.add(new SimpleMachineRecipe(this, "compressed_cobblestone_to_bedrock", new ItemStack(Material.BEDROCK), Items.OCTUPLE_COMPRESSED_COBBLESTONE.get(), 36000));
					}
				}
			}
		}

		return recipes;
	}


	@Override
	public String getTitle() {
		return this.tier.getName() + " Compressor";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.COMPRESSOR;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.PISTON;
	}


	@Override
	public String getMachineRegistryName() {
		return "compressor";
	}

}
