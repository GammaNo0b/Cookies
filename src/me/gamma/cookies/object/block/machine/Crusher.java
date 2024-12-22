
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Tag;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.recipe.machine.RandomOutputsRecipe;
import me.gamma.cookies.object.recipe.machine.SimpleMachineRecipe;



public class Crusher extends AbstractCraftingMachine {

	public Crusher(MachineTier tier) {
		super(tier);
	}


	@Override
	public Material getProgressMaterial(double progress) {
		switch (this.tier) {
			case BASIC:
				return Material.IRON_PICKAXE;
			case ADVANCED:
				return Material.GOLDEN_PICKAXE;
			case IMPROVED:
				return Material.DIAMOND_PICKAXE;
			case PERFECTED:
				return Material.NETHERITE_PICKAXE;
			default:
				return null;
		}
	}


	@Override
	public List<MachineRecipe> getMachineRecipes(TileState block) {
		List<MachineRecipe> recipes = new ArrayList<>();

		if(this.tier.getTier() > 0) {
			recipes.add(new SimpleMachineRecipe(this, "wool_to_string", new ItemStack(Material.STRING, 4), new MaterialChoice(Tag.WOOL), 100));
			recipes.add(new SimpleMachineRecipe(this, "clay_block_to_clay", new ItemStack(Material.CLAY_BALL, 4), new ItemStack(Material.CLAY), 160));
			recipes.add(new SimpleMachineRecipe(this, "dripstone_block_to_dripstone", new ItemStack(Material.POINTED_DRIPSTONE, 4), new ItemStack(Material.DRIPSTONE_BLOCK), 200));
			recipes.add(new SimpleMachineRecipe(this, "sugar_cane_to_sugar", new ItemStack(Material.SUGAR, 3), new ItemStack(Material.SUGAR_CANE), 80));
			recipes.add(new SimpleMachineRecipe(this, "melon_to_slices", new ItemStack(Material.MELON_SLICE, 9), new ItemStack(Material.MELON), 160));
			recipes.add(new SimpleMachineRecipe(this, "bone_to_bone_meal", new ItemStack(Material.BONE_MEAL, 6), new ItemStack(Material.BONE), 200));
			recipes.add(new SimpleMachineRecipe(this, "bamboo_block_to_bamboo", new ItemStack(Material.BAMBOO, 9), new ItemStack(Material.BAMBOO_BLOCK), 160));
			recipes.add(new SimpleMachineRecipe(this, "glowstone_to_dust", new ItemStack(Material.GLOWSTONE_DUST, 4), new ItemStack(Material.GLOWSTONE), 160));
			recipes.add(new RandomOutputsRecipe(this, "blaze_rods_to_blaze_powder", new ItemStack(Material.BLAZE_ROD), 100).addOutput(Material.BLAZE_POWDER, 1.0, 2, 4).addOutput(Material.BLAZE_POWDER, 0.25, 1, 2).addOutput(Items.SULFUR, 0.25, 1, 1));
			recipes.add(new SimpleMachineRecipe(this, "magma_block_to_magma_cream", new ItemStack(Material.MAGMA_CREAM, 4), new ItemStack(Material.MAGMA_BLOCK), 120));
			recipes.add(new SimpleMachineRecipe(this, "stone_to_cobble", new ItemStack(Material.COBBLESTONE), new ItemStack(Material.STONE), 200));
			recipes.add(new SimpleMachineRecipe(this, "cobble_to_gravel", new ItemStack(Material.GRAVEL), new ItemStack(Material.COBBLESTONE), 200));
			recipes.add(new SimpleMachineRecipe(this, "gravel_to_sand", new ItemStack(Material.SAND), new ItemStack(Material.GRAVEL), 160));
			recipes.add(new SimpleMachineRecipe(this, "sand_to_dust", Items.DUST.get(), new ItemStack(Material.SAND), 120));
			recipes.add(new SimpleMachineRecipe(this, "red_sand_to_red_dust", Items.RED_DUST.get(), new ItemStack(Material.RED_SAND), 120));
			recipes.add(new SimpleMachineRecipe(this, "blackstone_to_black_sand", Items.BLACK_SAND.get(), new ItemStack(Material.BLACKSTONE), 200));
			recipes.add(new SimpleMachineRecipe(this, "netherrack_to_crushed_netherrack", Items.CRUSHED_NETHERRACK.get(), new ItemStack(Material.NETHERRACK), 160));
			recipes.add(new SimpleMachineRecipe(this, "end_stone_to_crushed_end_stone", Items.CRUSHED_END_STONE.get(), new ItemStack(Material.END_STONE), 400));
			recipes.add(new SimpleMachineRecipe(this, "soul_sand_to_soul_dust", Items.SOUL_DUST.get(), 4, new ItemStack(Material.SOUL_SAND), 160));
			recipes.add(new SimpleMachineRecipe(this, "soul_soil_to_soul_dust", Items.SOUL_DUST.get(), 5, new ItemStack(Material.SOUL_SOIL), 160));
			recipes.add(new SimpleMachineRecipe(this, "coal_to_dust", Items.PULVERIZED_COAL.get(), new ItemStack(Material.COAL), 240));
			recipes.add(new SimpleMachineRecipe(this, "charcoal_to_dust", Items.PULVERIZED_COAL.get(), new ItemStack(Material.CHARCOAL), 240));
			recipes.add(new RandomOutputsRecipe(this, "steel_compound_to_dust", Items.STEEL_COMPOUND.get(), 300).addOutput(Items.STEEL_DUST, 1.0D, 1, 2).addOutput(Items.STEEL_DUST, 0.5D, 1, 1));

			if(this.tier.getTier() > 1) {
				recipes.add(new RandomOutputsRecipe(this, "quartz_to_dust", new ItemStack(Material.QUARTZ), 240).addOutput(Items.QUARTZ_DUST, 1.0D, 1, 3).addOutput(Items.QUARTZ_DUST, 0.5D, 1, 1));
				recipes.add(new SimpleMachineRecipe(this, "iron_ingot_to_dust", Items.IRON_DUST.get(), new ItemStack(Material.IRON_INGOT), 200));
				recipes.add(new SimpleMachineRecipe(this, "gold_ingot_to_dust", Items.GOLD_DUST.get(), new ItemStack(Material.GOLD_INGOT), 200));
				recipes.add(new SimpleMachineRecipe(this, "copper_ingot_to_dust", Items.COPPER_DUST.get(), new ItemStack(Material.COPPER_INGOT), 200));
				recipes.add(new SimpleMachineRecipe(this, "aluminum_ingot_to_dust", Items.ALUMINUM_DUST.get(), Items.ALUMINUM_INGOT.get(), 200));
				recipes.add(new SimpleMachineRecipe(this, "tin_ingot_to_dust", Items.TIN_DUST.get(), Items.TIN_INGOT.get(), 200));
				recipes.add(new SimpleMachineRecipe(this, "magnesium_ingot_to_dust", Items.MAGNESIUM_DUST.get(), Items.MAGNESIUM_INGOT.get(), 200));
				recipes.add(new SimpleMachineRecipe(this, "nickel_ingot_to_dust", Items.NICKEL_DUST.get(), Items.NICKEL_INGOT.get(), 200));
				recipes.add(new SimpleMachineRecipe(this, "lead_ingot_to_dust", Items.LEAD_DUST.get(), Items.LEAD_INGOT.get(), 200));
				recipes.add(new SimpleMachineRecipe(this, "silver_ingot_to_dust", Items.SILVER_DUST.get(), Items.SILVER_INGOT.get(), 200));
				recipes.add(new SimpleMachineRecipe(this, "steel_ingot_to_dust", Items.STEEL_DUST.get(), Items.STEEL_INGOT.get(), 200));
				recipes.add(new RandomOutputsRecipe(this, "raw_iron_to_iron_dust", new ItemStack(Material.RAW_IRON), 200).addOutput(Items.IRON_DUST, 1.0D, 1, 2));
				recipes.add(new RandomOutputsRecipe(this, "raw_copper_to_copper_dust", new ItemStack(Material.RAW_COPPER), 200).addOutput(Items.COPPER_DUST, 1.0D, 1, 2));
				recipes.add(new RandomOutputsRecipe(this, "raw_gold_to_gold_dust", new ItemStack(Material.RAW_GOLD), 200).addOutput(Items.GOLD_DUST, 1.0D, 1, 2));
				recipes.add(new SimpleMachineRecipe(this, "coal_block_to_coal", new ItemStack(Material.COAL, 9), new ItemStack(Material.COAL_BLOCK), 400));
				recipes.add(new SimpleMachineRecipe(this, "iron_block_to_iron_ingot", new ItemStack(Material.IRON_INGOT, 9), new ItemStack(Material.IRON_BLOCK), 400));
				recipes.add(new SimpleMachineRecipe(this, "gold_block_to_gold_ingot", new ItemStack(Material.GOLD_INGOT, 9), new ItemStack(Material.GOLD_BLOCK), 400));
				recipes.add(new SimpleMachineRecipe(this, "redstone_block_to_redstone", new ItemStack(Material.REDSTONE, 9), new ItemStack(Material.REDSTONE_BLOCK), 400));
				recipes.add(new SimpleMachineRecipe(this, "lapis_block_to_lapis", new ItemStack(Material.LAPIS_LAZULI, 9), new ItemStack(Material.LAPIS_BLOCK), 400));
				recipes.add(new SimpleMachineRecipe(this, "emerald_block_to_emerald", new ItemStack(Material.EMERALD, 9), new ItemStack(Material.EMERALD_BLOCK), 400));
				recipes.add(new SimpleMachineRecipe(this, "diamond_block_to_diamond", new ItemStack(Material.DIAMOND, 9), new ItemStack(Material.DIAMOND_BLOCK), 400));
				recipes.add(new SimpleMachineRecipe(this, "netherite_block_to_netherite_ingot", new ItemStack(Material.NETHERITE_INGOT, 9), new ItemStack(Material.NETHERITE_BLOCK), 600));
				recipes.add(new SimpleMachineRecipe(this, "quartz_block_to_quartz", new ItemStack(Material.QUARTZ, 4), new ItemStack(Material.QUARTZ_BLOCK), 200));
				recipes.add(new RandomOutputsRecipe(this, "small_amethyst_cluster_to_shards", new ItemStack(Material.SMALL_AMETHYST_BUD), 160).addOutput(Material.AMETHYST_SHARD, 1.0D, 1, 3).addOutput(Material.AMETHYST_SHARD, 0.5D, 1, 1));
				recipes.add(new RandomOutputsRecipe(this, "small_amethyst_cluster_to_shards", new ItemStack(Material.MEDIUM_AMETHYST_BUD), 160).addOutput(Material.AMETHYST_SHARD, 1.0D, 2, 6).addOutput(Material.AMETHYST_SHARD, 0.5D, 1, 2));
				recipes.add(new RandomOutputsRecipe(this, "small_amethyst_cluster_to_shards", new ItemStack(Material.LARGE_AMETHYST_BUD), 160).addOutput(Material.AMETHYST_SHARD, 1.0D, 3, 9).addOutput(Material.AMETHYST_SHARD, 0.5D, 1, 3));
				recipes.add(new RandomOutputsRecipe(this, "small_amethyst_cluster_to_shards", new ItemStack(Material.AMETHYST_CLUSTER), 160).addOutput(Material.AMETHYST_SHARD, 1.0D, 4, 12).addOutput(Material.AMETHYST_SHARD, 0.5D, 1, 4));
				recipes.add(new SimpleMachineRecipe(this, "amethyst_block_to_shards", new ItemStack(Material.AMETHYST_SHARD, 4), new ItemStack(Material.AMETHYST_BLOCK), 200));

				if(this.tier.getTier() > 2) {
					recipes.add(new RandomOutputsRecipe(this, "coal_ore_to_coal", new ItemStack(Material.COAL_ORE), 200).addOutput(Material.COAL, 1.0D, 1, 3).addOutput(Material.COAL, 0.5D, 1, 1).addOutput(Material.COBBLESTONE, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "deepslate_coal_ore_to_coal", new ItemStack(Material.DEEPSLATE_COAL_ORE), 300).addOutput(Material.COAL, 1.0D, 2, 4).addOutput(Material.COAL, 0.5D, 1, 2).addOutput(Material.DIAMOND, 0.01D, 1, 1).addOutput(Material.COBBLED_DEEPSLATE, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "iron_ore_to_raw_iron", new ItemStack(Material.IRON_ORE), 200).addOutput(Material.RAW_IRON, 1.0D, 1, 3).addOutput(Material.RAW_IRON, 0.5D, 1, 1).addOutput(Material.COBBLESTONE, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "deepslate_iron_ore_to_raw_iron", new ItemStack(Material.DEEPSLATE_IRON_ORE), 300).addOutput(Material.RAW_IRON, 1.0D, 2, 4).addOutput(Material.RAW_IRON, 0.5D, 1, 1).addOutput(Material.COBBLED_DEEPSLATE, 0.1D, 1, 1));
					recipes.add(new SimpleMachineRecipe(this, "raw_iron_block_to_raw_iron", new ItemStack(Material.RAW_IRON, 9), new ItemStack(Material.RAW_IRON_BLOCK), 180));
					recipes.add(new RandomOutputsRecipe(this, "copper_ore_to_raw_copper", new ItemStack(Material.COPPER_ORE), 200).addOutput(Material.RAW_COPPER, 1.0D, 1, 5).addOutput(Material.RAW_COPPER, 0.5D, 1, 2).addOutput(Material.COBBLESTONE, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "deepslate_copper_ore_to_raw_copper", new ItemStack(Material.DEEPSLATE_COPPER_ORE), 300).addOutput(Material.RAW_COPPER, 1.0D, 2, 6).addOutput(Material.RAW_COPPER, 0.5D, 1, 2).addOutput(Material.COBBLED_DEEPSLATE, 0.1D, 1, 1));
					recipes.add(new SimpleMachineRecipe(this, "raw_copper_block_to_raw_copper", new ItemStack(Material.RAW_COPPER, 9), new ItemStack(Material.RAW_COPPER_BLOCK), 180));
					recipes.add(new RandomOutputsRecipe(this, "gold_ore_to_raw_gold", new ItemStack(Material.GOLD_ORE), 200).addOutput(Material.RAW_GOLD, 1.0D, 1, 3).addOutput(Material.RAW_GOLD, 0.5D, 1, 1).addOutput(Material.COBBLESTONE, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "deepslate_gold_ore_to_raw_gold", new ItemStack(Material.DEEPSLATE_GOLD_ORE), 300).addOutput(Material.RAW_GOLD, 1.0D, 1, 4).addOutput(Material.RAW_GOLD, 0.5D, 1, 1).addOutput(Material.COBBLED_DEEPSLATE, 0.1D, 1, 1));
					recipes.add(new SimpleMachineRecipe(this, "raw_gold_block_to_raw_gold", new ItemStack(Material.RAW_GOLD, 9), new ItemStack(Material.RAW_GOLD_BLOCK), 180));
					recipes.add(new RandomOutputsRecipe(this, "redstone_ore_to_redstone", new ItemStack(Material.REDSTONE_ORE), 200).addOutput(Material.REDSTONE, 1.0D, 3, 8).addOutput(Material.REDSTONE, 0.5D, 1, 3).addOutput(Material.COBBLESTONE, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "deepslate_redstone_ore_to_redstone", new ItemStack(Material.DEEPSLATE_REDSTONE_ORE), 300).addOutput(Material.REDSTONE, 1.0D, 4, 10).addOutput(Material.REDSTONE, 0.5D, 1, 4).addOutput(Material.COBBLED_DEEPSLATE, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "lapis_ore_to_lapis_lazuli", new ItemStack(Material.LAPIS_ORE), 200).addOutput(Material.LAPIS_LAZULI, 1.0D, 2, 6).addOutput(Material.LAPIS_LAZULI, 0.5D, 1, 3).addOutput(Material.COBBLESTONE, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "deepslate_lapis_ore_to_lapis_lazuli", new ItemStack(Material.DEEPSLATE_LAPIS_ORE), 300).addOutput(Material.LAPIS_LAZULI, 1.0D, 3, 7).addOutput(Material.LAPIS_LAZULI, 0.5D, 1, 3).addOutput(Material.COBBLED_DEEPSLATE, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "emerald_ore_to_emeralds", new ItemStack(Material.EMERALD_ORE), 200).addOutput(Material.EMERALD, 1.0D, 1, 2).addOutput(Material.EMERALD, 0.5D, 1, 1).addOutput(Material.COBBLESTONE, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "deepslate_emerald_ore_to_emeralds", new ItemStack(Material.DEEPSLATE_EMERALD_ORE), 300).addOutput(Material.EMERALD, 1.0D, 1, 3).addOutput(Material.EMERALD, 0.5D, 1, 1).addOutput(Material.COBBLED_DEEPSLATE, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "diamond_ore_to_diamonds", new ItemStack(Material.DIAMOND_ORE), 200).addOutput(Material.DIAMOND, 1.0D, 1, 2).addOutput(Material.DIAMOND, 0.5D, 1, 1).addOutput(Material.COBBLESTONE, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "deepslate_diamond_ore_to_diamonds", new ItemStack(Material.DEEPSLATE_DIAMOND_ORE), 300).addOutput(Material.DIAMOND, 1.0D, 1, 3).addOutput(Material.DIAMOND, 0.5D, 1, 1).addOutput(Material.COBBLED_DEEPSLATE, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "nether_gold_ore_to_gold_nuggets", new ItemStack(Material.NETHER_GOLD_ORE), 200).addOutput(Material.GOLD_NUGGET, 1.0D, 3, 9).addOutput(Material.GOLD_NUGGET, 0.5D, 1, 3).addOutput(Material.RAW_GOLD, 0.1D, 1, 1).addOutput(Items.CRUSHED_NETHERRACK, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "nether_quartz_ore_to_quartz", new ItemStack(Material.NETHER_QUARTZ_ORE), 200).addOutput(Material.QUARTZ, 1.0D, 1, 3).addOutput(Material.QUARTZ, 0.5D, 1, 1).addOutput(Items.CRUSHED_NETHERRACK, 0.1D, 1, 1));
					recipes.add(new RandomOutputsRecipe(this, "ancient_debris_to_netherite_scrap", new ItemStack(Material.ANCIENT_DEBRIS), 300).addOutput(Material.NETHERITE_SCRAP, 1.0D, 1, 2).addOutput(Material.NETHERITE_SCRAP, 0.1D, 1, 1));

					if(this.tier.getTier() > 3) {
						recipes.add(new RandomOutputsRecipe(this, "obsidian_to_crushed_obsidian", new ItemStack(Material.OBSIDIAN), 400).addOutput(Items.CRUSHED_OBSIDIAN, 1.0D, 1, 3));
						recipes.add(new RandomOutputsRecipe(this, "crying_obsidian_to_crushed_crying_obsidian", new ItemStack(Material.CRYING_OBSIDIAN), 400).addOutput(Items.CRUSHED_CRYING_OBSIDIAN, 1.0D, 1, 2).addOutput(Items.CRUSHED_OBSIDIAN, 0.25D, 1, 1));
					}
				}
			}
		}

		return recipes;
	}


	@Override
	public String getTitle() {
		return this.tier.getName() + " Crusher";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CRUSHER;
	}


	@Override
	public String getMachineRegistryName() {
		return "crusher";
	}


	@Override
	protected void proceed(TileState block, int progress, int goal) {
		block.getWorld().spawnParticle(Particle.DUST, block.getLocation().add(0.5D, 0.5D, 0.5D), 1, 0.25D, 0.25D, 0.25D, 1, new DustOptions(Color.fromBGR(0x3F3F3F), 1.0F));
	}

}
