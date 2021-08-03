
package me.gamma.cookies.objects.block.skull.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.machine.MachineTier;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.list.TieredMaterials;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.MachineRecipe;
import me.gamma.cookies.objects.recipe.RandomOutputRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;



public class MineralExtractor extends AbstractSkullMachine {

	public MineralExtractor(MachineTier tier) {
		super(tier);
	}


	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>();

		if(tier.getTier() > 0) {
			recipes.add(new RandomOutputRecipe("dirt_to_resources", new ItemStack(Material.DIRT), 80).addOutput(Material.FLINT, 20).addOutput(Material.WHEAT_SEEDS, 2).addOutput(CustomItemSetup.POTATO_SEEDS.createDefaultItemStack(), 2).addOutput(CustomItemSetup.CARROT_SEEDS.createDefaultItemStack(), 2).addOutput(Material.BEETROOT_SEEDS, 2).addOutput(Material.PUMPKIN_SEEDS, 1).addOutput(Material.MELON_SEEDS, 1).addOutput(CustomItemSetup.SUGAR_CANE_SEEDS.createDefaultItemStack(), 2).addOutput(Material.GRASS, 5).addOutput(CustomBlockSetup.STONE_PEBBLE.createDefaultItemStack(), 10).addOutput(CustomBlockSetup.GRANITE_PEBBLE.createDefaultItemStack(), 10).addOutput(CustomBlockSetup.DIORITE_PEBBLE.createDefaultItemStack(), 10).addOutput(CustomBlockSetup.ANDESITE_PEBBLE.createDefaultItemStack(), 10));
			recipes.add(new RandomOutputRecipe("coarse_dirt_to_resources", new ItemStack(Material.COARSE_DIRT), 80).addOutput(Material.GRAVEL, 5).addOutput(Material.FLINT, 20).addOutput(CustomBlockSetup.STONE_PEBBLE.createDefaultItemStack(), 10).addOutput(CustomBlockSetup.GRANITE_PEBBLE.createDefaultItemStack(), 10).addOutput(CustomBlockSetup.DIORITE_PEBBLE.createDefaultItemStack(), 10).addOutput(CustomBlockSetup.ANDESITE_PEBBLE.createDefaultItemStack(), 10));
			recipes.add(new RandomOutputRecipe("sand_to_dusts", new ItemStack(Material.SAND), 80).addOutput(Material.AIR, 50).addOutput(CustomItemSetup.CACTUS_SEEDS.createDefaultItemStack(), 5).addOutput(CustomItemSetup.SUGAR_CANE_SEEDS.createDefaultItemStack(), 5).addOutput(Material.DEAD_BUSH, 3));
			recipes.add(new RandomOutputRecipe("red_sand_to_dusts", new ItemStack(Material.RED_SAND), 80).addOutput(Material.AIR, 50).addOutput(CustomItemSetup.CACTUS_SEEDS.createDefaultItemStack(), 10).addOutput(CustomItemSetup.SUGAR_CANE_SEEDS.createDefaultItemStack(), 10).addOutput(Material.DEAD_BUSH, 5).addOutput(Material.GOLD_NUGGET, 2).addOutput(Material.RAW_GOLD, 1));

			if(tier.getTier() > 1) {
				recipes.add(new RandomOutputRecipe("gravel_to_dusts", new ItemStack(Material.GRAVEL), 160).addOutput(CustomItemSetup.IRON_DUST.createDefaultItemStack(), 10).addOutput(CustomItemSetup.GOLD_DUST.createDefaultItemStack(), 2).addOutput(CustomItemSetup.COPPER_DUST.createDefaultItemStack(), 12).addOutput(CustomItemSetup.ALUMINUM_DUST.createDefaultItemStack(), 6).addOutput(CustomItemSetup.NICKEL_DUST.createDefaultItemStack(), 4).addOutput(CustomItemSetup.LEAD_DUST.createDefaultItemStack(), 1).addOutput(CustomItemSetup.SILVER_DUST.createDefaultItemStack(), 1));

				if(tier.getTier() > 2) {
					recipes.add(new RandomOutputRecipe("soulsand_to_resources", new ItemStack(Material.SOUL_SAND), 120).addOutput(Material.AIR, 20).addOutput(Material.BLAZE_POWDER, 3).addOutput(Material.GHAST_TEAR, 1).addOutput(Material.NETHER_WART, 2).addOutput(Material.QUARTZ, 5).addOutput(Material.GOLD_NUGGET, 5));
					recipes.add(new RandomOutputRecipe("soulsoil_to_resources", new ItemStack(Material.SOUL_SOIL), 160).addOutput(Material.AIR, 30).addOutput(Material.GHAST_TEAR, 10).addOutput(Material.BONE, 15).addOutput(Material.ENDER_PEARL, 5));

					if(tier.getTier() > 3) {
						recipes.add(new RandomOutputRecipe("crushed_netherrack_to_resources", CustomBlockSetup.CRUSHED_NETHERRACK.createDefaultItemStack(), 160).addOutput(Material.AIR, 100).addOutput(CustomItemSetup.SOUL_DUST.createDefaultItemStack(), 50).addOutput(Material.BLAZE_POWDER, 10).addOutput(Material.GOLD_NUGGET, 15).addOutput(Material.QUARTZ, 15).addOutput(Material.ANCIENT_DEBRIS, 1));
						recipes.add(new RandomOutputRecipe("crushed_end_stone_to_resources", CustomBlockSetup.CRUSHED_END_STONE.createDefaultItemStack(), 200).addOutput(Material.AIR, 200).addOutput(Material.CHORUS_FRUIT, 20).addOutput(Material.CHORUS_FLOWER, 2).addOutput(Material.ENDER_PEARL, 5).addOutput(Material.SHULKER_SHELL, 1));
						recipes.add(new RandomOutputRecipe("crushed_obsidian_to_resources", CustomBlockSetup.CRUSHED_OBSIDIAN.createDefaultItemStack(), 240).addOutput(Material.COAL, 100).addOutput(Material.REDSTONE, 40).addOutput(Material.LAPIS_LAZULI, 20).addOutput(Material.EMERALD, 1).addOutput(Material.DIAMOND, 1));
						recipes.add(new RandomOutputRecipe("crushed_crying_obsidian_to_resources", CustomBlockSetup.CRUSHED_CRYING_OBSIDIAN.createDefaultItemStack(), 240).addOutput(Material.COAL, 100).addOutput(Material.REDSTONE, 50).addOutput(Material.LAPIS_LAZULI, 40).addOutput(Material.EMERALD, 10).addOutput(Material.DIAMOND, 10).addOutput(Material.ENDER_PEARL, 5).addOutput(CustomBlockSetup.CRUSHED_OBSIDIAN.createDefaultItemStack(), 5));
					}
				}
			}
		}

		return recipes;
	}


	@Override
	public ItemStack getProgressIcon() {
		switch (tier) {
			case BASIC:
				return new ItemStack(Material.IRON_SHOVEL);
			case ADVANCED:
				return new ItemStack(Material.GOLDEN_SHOVEL);
			case IMPROVED:
				return new ItemStack(Material.DIAMOND_SHOVEL);
			case PERFECTED:
				return new ItemStack(Material.NETHERITE_SHOVEL);
			default:
				return null;
		}
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.MINERAL_EXTRACTOR;
	}


	@Override
	public String getMachineIdentifier() {
		return "mineral_extractor";
	}


	@Override
	public String getDisplayName() {
		return tier.getName() + " §2Mineral Extractor";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" T ", "MCH", "SSS");
		ItemStack top = null;
		switch (this.tier) {
			case BASIC:
				top = new ItemStack(Material.HOPPER);
				break;
			case ADVANCED:
				top = CustomBlockSetup.MINERAL_EXTRACTOR.createDefaultItemStack();
				break;
			case IMPROVED:
				top = CustomBlockSetup.ADVANCED_MINERAL_EXTRACTOR.createDefaultItemStack();
				break;
			case PERFECTED:
				top = CustomBlockSetup.IMPROVED_MINERAL_EXTRACTOR.createDefaultItemStack();
				break;
			default:
				break;
		}
		recipe.setIngredient('T', top);
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('C', TieredMaterials.getCore(tier));
		recipe.setIngredient('H', CustomBlockSetup.COPPER_COIL.createDefaultItemStack());
		recipe.setIngredient('S', TieredMaterials.getIngot(tier));
		return recipe;
	}

}
