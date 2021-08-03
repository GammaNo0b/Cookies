
package me.gamma.cookies.objects.block.skull.machine;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.machine.MachineTier;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.list.TieredMaterials;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.MachineRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.objects.recipe.SimpleMachineRecipe;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;



public class Crusher extends AbstractSkullMachine {

	public Crusher(MachineTier tier) {
		super(tier);
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
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>();

		if(tier.getTier() > 0) {
			recipes.add(new SimpleMachineRecipe("wool_to_string", new ItemStack(Material.STRING, 4), new ItemStack(Material.WHITE_WOOL), 80));
			recipes.add(new SimpleMachineRecipe("clay_block_to_clay", new ItemStack(Material.CLAY_BALL, 4), new ItemStack(Material.CLAY), 80));
			recipes.add(new SimpleMachineRecipe("dropstone_block_to_dripstone", new ItemStack(Material.POINTED_DRIPSTONE, 4), new ItemStack(Material.DRIPSTONE_BLOCK), 160));
			recipes.add(new SimpleMachineRecipe("melon_to_slices", new ItemStack(Material.MELON_SLICE, 9), new ItemStack(Material.MELON), 120));
			recipes.add(new SimpleMachineRecipe("glowstone_to_dust", new ItemStack(Material.GLOWSTONE_DUST, 4), new ItemStack(Material.GLOWSTONE), 120));
			recipes.add(new SimpleMachineRecipe("blaze_rods_to_blaze_powder", new ItemStack(Material.BLAZE_POWDER, 4), new ItemStack(Material.BLAZE_ROD), 80));
			recipes.add(new SimpleMachineRecipe("magma_block_to_magma_cream", new ItemStack(Material.MAGMA_CREAM, 4), new ItemStack(Material.MAGMA_BLOCK), 120));
			recipes.add(new SimpleMachineRecipe("stone_to_cobble", new ItemStack(Material.COBBLESTONE), new ItemStack(Material.STONE), 200));
			recipes.add(new SimpleMachineRecipe("cobble_to_gravel", new ItemStack(Material.GRAVEL), new ItemStack(Material.COBBLESTONE), 160));
			recipes.add(new SimpleMachineRecipe("gravel_to_sand", new ItemStack(Material.SAND), new ItemStack(Material.GRAVEL), 120));
			recipes.add(new SimpleMachineRecipe("netherrack_to_crushed_netherrack", CustomBlockSetup.CRUSHED_NETHERRACK.createDefaultItemStack(), new ItemStack(Material.NETHERRACK), 160));
			recipes.add(new SimpleMachineRecipe("end_stone_to_crushed_end_stone", CustomBlockSetup.CRUSHED_END_STONE.createDefaultItemStack(), new ItemStack(Material.END_STONE), 240));
			recipes.add(new SimpleMachineRecipe("soul_sand_to_soul_dust", CustomItemSetup.SOUL_DUST.createDefaultItemStack(), 4, new ItemStack(Material.SOUL_SAND), 120));
			recipes.add(new SimpleMachineRecipe("soul_soil_to_soul_dust", CustomItemSetup.SOUL_DUST.createDefaultItemStack(), 5, new ItemStack(Material.SOUL_SOIL), 120));

			if(tier.getTier() > 1) {
				recipes.add(new SimpleMachineRecipe("coal_to_dust", CustomItemSetup.COAL_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, new ItemStack(Material.COAL), 160));
				recipes.add(new SimpleMachineRecipe("iron_ingot_to_dust", CustomItemSetup.IRON_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, new ItemStack(Material.IRON_INGOT), 160));
				recipes.add(new SimpleMachineRecipe("gold_ingot_to_dust", CustomItemSetup.GOLD_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, new ItemStack(Material.GOLD_INGOT), 160));
				recipes.add(new SimpleMachineRecipe("copper_ingot_to_dust", CustomItemSetup.COPPER_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, new ItemStack(Material.COPPER_INGOT), 160));
				recipes.add(new SimpleMachineRecipe("aluminum_ingot_to_dust", CustomItemSetup.ALUMINUM_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, CustomItemSetup.ALUMINUM_INGOT.createDefaultItemStack(), 160));
				recipes.add(new SimpleMachineRecipe("nickel_ingot_to_dust", CustomItemSetup.NICKEL_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, CustomItemSetup.NICKEL_INGOT.createDefaultItemStack(), 160));
				recipes.add(new SimpleMachineRecipe("lead_ingot_to_dust", CustomItemSetup.LEAD_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, CustomItemSetup.LEAD_INGOT.createDefaultItemStack(), 160));
				recipes.add(new SimpleMachineRecipe("silver_ingot_to_dust", CustomItemSetup.SILVER_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, CustomItemSetup.SILVER_INGOT.createDefaultItemStack(), 160));
				recipes.add(new SimpleMachineRecipe("steel_ingot_to_dust", CustomItemSetup.STEEL_DUST.createDefaultItemStack(), RecipeCategory.RESOURCES, CustomItemSetup.STEEL_INGOT.createDefaultItemStack(), 160));
				recipes.add(new SimpleMachineRecipe("netherite_block_to_netherite_ingot", new ItemStack(Material.NETHERITE_INGOT, 9), new ItemStack(Material.NETHERITE_BLOCK), 360));
				recipes.add(new SimpleMachineRecipe("raw_iron_to_iron_dust", CustomItemSetup.IRON_DUST.createDefaultItemStack(), 2, new ItemStack(Material.RAW_IRON), 160));
				recipes.add(new SimpleMachineRecipe("raw_copper_to_copper_dust", CustomItemSetup.COPPER_DUST.createDefaultItemStack(), 2, new ItemStack(Material.RAW_COPPER), 160));
				recipes.add(new SimpleMachineRecipe("raw_gold_to_gold_dust", CustomItemSetup.GOLD_DUST.createDefaultItemStack(), 2, new ItemStack(Material.RAW_GOLD), 160));
				recipes.add(new SimpleMachineRecipe("coal_block_to_coal", new ItemStack(Material.COAL, 9), new ItemStack(Material.COAL_BLOCK), 160));
				recipes.add(new SimpleMachineRecipe("iron_block_to_iron_ingot", new ItemStack(Material.IRON_INGOT, 9), new ItemStack(Material.IRON_BLOCK), 160));
				recipes.add(new SimpleMachineRecipe("gold_block_to_gold_ingot", new ItemStack(Material.GOLD_INGOT, 9), new ItemStack(Material.GOLD_BLOCK), 160));
				recipes.add(new SimpleMachineRecipe("redstone_block_to_redstone", new ItemStack(Material.REDSTONE, 9), new ItemStack(Material.REDSTONE_BLOCK), 180));
				recipes.add(new SimpleMachineRecipe("lapis_block_to_lapis", new ItemStack(Material.LAPIS_LAZULI, 9), new ItemStack(Material.LAPIS_BLOCK), 200));
				recipes.add(new SimpleMachineRecipe("emerald_block_to_emerald", new ItemStack(Material.EMERALD, 9), new ItemStack(Material.EMERALD_BLOCK), 240));
				recipes.add(new SimpleMachineRecipe("diamond_block_to_diamond", new ItemStack(Material.DIAMOND, 9), new ItemStack(Material.DIAMOND_BLOCK), 240));

				if(tier.getTier() > 2) {
					recipes.add(new SimpleMachineRecipe("coal_ore_to_coal", new ItemStack(Material.COAL, 2), new ItemStack(Material.COAL_ORE), 160));
					recipes.add(new SimpleMachineRecipe("deepslate_coal_ore_to_coal", new ItemStack(Material.COAL, 3), new ItemStack(Material.DEEPSLATE_COAL_ORE), 200));
					recipes.add(new SimpleMachineRecipe("iron_ore_to_raw_iron", new ItemStack(Material.RAW_IRON, 2), new ItemStack(Material.IRON_ORE), 160));
					recipes.add(new SimpleMachineRecipe("deepslate_iron_ore_to_raw_iron", new ItemStack(Material.RAW_IRON, 3), new ItemStack(Material.DEEPSLATE_IRON_ORE), 200));
					recipes.add(new SimpleMachineRecipe("iron_ore_to_raw_iron", new ItemStack(Material.RAW_COPPER, 3), new ItemStack(Material.COPPER_ORE), 160));
					recipes.add(new SimpleMachineRecipe("deepslate_iron_ore_to_raw_iron", new ItemStack(Material.RAW_COPPER, 4), new ItemStack(Material.DEEPSLATE_COPPER_ORE), 200));
					recipes.add(new SimpleMachineRecipe("gold_ore_to_raw_gold", new ItemStack(Material.RAW_GOLD, 2), new ItemStack(Material.GOLD_ORE), 160));
					recipes.add(new SimpleMachineRecipe("deepslate_gold_ore_to_raw_gold", new ItemStack(Material.RAW_GOLD, 3), new ItemStack(Material.DEEPSLATE_GOLD_ORE), 200));
					recipes.add(new SimpleMachineRecipe("redstone_ore_to_redstone", new ItemStack(Material.REDSTONE, 12), new ItemStack(Material.REDSTONE_ORE), 160));
					recipes.add(new SimpleMachineRecipe("deepslate_redstone_ore_to_redstone", new ItemStack(Material.REDSTONE, 15), new ItemStack(Material.REDSTONE_ORE), 200));
					recipes.add(new SimpleMachineRecipe("lapis_ore_to_lapis_lazuli", new ItemStack(Material.LAPIS_LAZULI, 10), new ItemStack(Material.LAPIS_ORE), 160));
					recipes.add(new SimpleMachineRecipe("deepslate_lapis_ore_to_lapis_lazuli", new ItemStack(Material.LAPIS_LAZULI, 12), new ItemStack(Material.DEEPSLATE_LAPIS_ORE), 200));
					recipes.add(new SimpleMachineRecipe("emerald_ore_to_emeralds", new ItemStack(Material.EMERALD, 2), new ItemStack(Material.EMERALD_ORE), 160));
					recipes.add(new SimpleMachineRecipe("deepslate_emerald_ore_to_emeralds", new ItemStack(Material.EMERALD, 3), new ItemStack(Material.DEEPSLATE_EMERALD_ORE), 200));
					recipes.add(new SimpleMachineRecipe("diamond_ore_to_diamonds", new ItemStack(Material.DIAMOND, 2), new ItemStack(Material.DIAMOND_ORE), 160));
					recipes.add(new SimpleMachineRecipe("deepslate_diamond_ore_to_diamonds", new ItemStack(Material.DIAMOND, 3), new ItemStack(Material.DEEPSLATE_DIAMOND_ORE), 200));
					recipes.add(new SimpleMachineRecipe("nether_gold_ore_to_gold_nuggets", new ItemStack(Material.GOLD_NUGGET, 12), new ItemStack(Material.NETHER_GOLD_ORE), 160));
					recipes.add(new SimpleMachineRecipe("nether_quartz_ore_to_quartz", new ItemStack(Material.QUARTZ, 3), new ItemStack(Material.NETHER_QUARTZ_ORE), 160));
					recipes.add(new SimpleMachineRecipe("ancient_debris_to_netherite_scrap", new ItemStack(Material.NETHERITE_SCRAP, 2), new ItemStack(Material.ANCIENT_DEBRIS), 160));

					if(tier.getTier() > 3) {
						recipes.add(new SimpleMachineRecipe("obsidian_to_crushed_obsidian", CustomBlockSetup.CRUSHED_OBSIDIAN.createDefaultItemStack(), new ItemStack(Material.OBSIDIAN), 320));
						recipes.add(new SimpleMachineRecipe("crying_obsidian_to_crushed_crying_obsidian", CustomBlockSetup.CRUSHED_CRYING_OBSIDIAN.createDefaultItemStack(), new ItemStack(Material.CRYING_OBSIDIAN), 320));
					}
				}
			}
		}

		return recipes;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CRUSHER;
	}


	@Override
	public String getMachineIdentifier() {
		return "crusher";
	}


	@Override
	public String getDisplayName() {
		return tier.getName() + " §8Crusher";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Crushes various Materials to more dustier ones.");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" H ", "PCP", "MAM");
		ItemStack top = null;
		switch (this.tier) {
			case BASIC:
				top = new ItemStack(Material.HOPPER);
				break;
			case ADVANCED:
				top = CustomBlockSetup.CRUSHER.createDefaultItemStack();
				break;
			case IMPROVED:
				top = CustomBlockSetup.ADVANCED_CRUSHER.createDefaultItemStack();
				break;
			case PERFECTED:
				top = CustomBlockSetup.IMPROVED_CRUSHER.createDefaultItemStack();
				break;
			default:
				break;
		}
		recipe.setIngredient('H', top);
		recipe.setIngredient('P', Material.IRON_PICKAXE);
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('A', TieredMaterials.getIngot(tier));
		recipe.setIngredient('C', TieredMaterials.getCore(tier));
		return recipe;
	}

}
