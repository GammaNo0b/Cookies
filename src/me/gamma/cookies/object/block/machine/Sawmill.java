
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.recipe.machine.SimpleMachineRecipe;



public class Sawmill extends AbstractCraftingMachine {

	public Sawmill(MachineTier tier) {
		super(tier);
	}


	@Override
	public List<MachineRecipe> getMachineRecipes(TileState block) {
		List<MachineRecipe> recipes = new ArrayList<>();

		if(this.tier.getTier() > 0) {
			recipes.add(new SimpleMachineRecipe(this, "stripped_oak_log", new ItemStack(Material.STRIPPED_OAK_LOG), new RecipeChoice.MaterialChoice(Material.OAK_LOG), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_oak_wood", new ItemStack(Material.STRIPPED_OAK_WOOD), new RecipeChoice.MaterialChoice(Material.OAK_WOOD), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_spruce_log", new ItemStack(Material.STRIPPED_SPRUCE_LOG), new RecipeChoice.MaterialChoice(Material.SPRUCE_LOG), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_spruce_wood", new ItemStack(Material.STRIPPED_SPRUCE_WOOD), new RecipeChoice.MaterialChoice(Material.SPRUCE_WOOD), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_birch_log", new ItemStack(Material.STRIPPED_BIRCH_LOG), new RecipeChoice.MaterialChoice(Material.BIRCH_LOG), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_birch_wood", new ItemStack(Material.STRIPPED_BIRCH_WOOD), new RecipeChoice.MaterialChoice(Material.BIRCH_WOOD), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_dark_oak_log", new ItemStack(Material.STRIPPED_DARK_OAK_LOG), new RecipeChoice.MaterialChoice(Material.DARK_OAK_LOG), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_dark_oak_wood", new ItemStack(Material.STRIPPED_DARK_OAK_WOOD), new RecipeChoice.MaterialChoice(Material.DARK_OAK_WOOD), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_acacia_log", new ItemStack(Material.STRIPPED_ACACIA_LOG), new RecipeChoice.MaterialChoice(Material.ACACIA_LOG), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_acacia_wood", new ItemStack(Material.STRIPPED_ACACIA_WOOD), new RecipeChoice.MaterialChoice(Material.ACACIA_WOOD), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_jungle_log", new ItemStack(Material.STRIPPED_JUNGLE_LOG), new RecipeChoice.MaterialChoice(Material.JUNGLE_LOG), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_jungle_wood", new ItemStack(Material.STRIPPED_JUNGLE_WOOD), new RecipeChoice.MaterialChoice(Material.JUNGLE_WOOD), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_mangrove_log", new ItemStack(Material.STRIPPED_MANGROVE_LOG), new RecipeChoice.MaterialChoice(Material.MANGROVE_LOG), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_mangrove_wood", new ItemStack(Material.STRIPPED_MANGROVE_WOOD), new RecipeChoice.MaterialChoice(Material.MANGROVE_WOOD), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_cherry_log", new ItemStack(Material.STRIPPED_CHERRY_LOG), new RecipeChoice.MaterialChoice(Material.CHERRY_LOG), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_cherry_wood", new ItemStack(Material.STRIPPED_CHERRY_WOOD), new RecipeChoice.MaterialChoice(Material.CHERRY_WOOD), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_pale_oak_log", new ItemStack(Material.STRIPPED_PALE_OAK_LOG), new RecipeChoice.MaterialChoice(Material.PALE_OAK_LOG), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_pale_oak_wood", new ItemStack(Material.STRIPPED_PALE_OAK_WOOD), new RecipeChoice.MaterialChoice(Material.PALE_OAK_WOOD), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_bamboo_block", new ItemStack(Material.STRIPPED_BAMBOO_BLOCK), new RecipeChoice.MaterialChoice(Material.BAMBOO_BLOCK), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_crimson_stem", new ItemStack(Material.STRIPPED_CRIMSON_STEM), new RecipeChoice.MaterialChoice(Material.CRIMSON_STEM), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_crimson_hyphae", new ItemStack(Material.STRIPPED_CRIMSON_HYPHAE), new RecipeChoice.MaterialChoice(Material.CRIMSON_HYPHAE), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_warped_stem", new ItemStack(Material.STRIPPED_WARPED_STEM), new RecipeChoice.MaterialChoice(Material.WARPED_STEM), 160));
			recipes.add(new SimpleMachineRecipe(this, "stripped_warped_hyphae", new ItemStack(Material.STRIPPED_WARPED_HYPHAE), new RecipeChoice.MaterialChoice(Material.WARPED_HYPHAE), 160));

			if(this.tier.getTier() > 1) {
				recipes.add(new SimpleMachineRecipe(this, "oak_planks", new ItemStack(Material.OAK_PLANKS, 6), new RecipeChoice.MaterialChoice(Material.STRIPPED_OAK_LOG, Material.STRIPPED_OAK_WOOD), 160));
				recipes.add(new SimpleMachineRecipe(this, "spruce_planks", new ItemStack(Material.SPRUCE_PLANKS, 6), new RecipeChoice.MaterialChoice(Material.STRIPPED_SPRUCE_LOG, Material.STRIPPED_SPRUCE_WOOD), 160));
				recipes.add(new SimpleMachineRecipe(this, "birch_planks", new ItemStack(Material.BIRCH_PLANKS, 6), new RecipeChoice.MaterialChoice(Material.STRIPPED_BIRCH_LOG, Material.STRIPPED_BIRCH_WOOD), 160));
				recipes.add(new SimpleMachineRecipe(this, "dark_oak_planks", new ItemStack(Material.DARK_OAK_PLANKS, 6), new RecipeChoice.MaterialChoice(Material.STRIPPED_DARK_OAK_LOG, Material.STRIPPED_DARK_OAK_WOOD), 160));
				recipes.add(new SimpleMachineRecipe(this, "acacia_planks", new ItemStack(Material.ACACIA_PLANKS, 6), new RecipeChoice.MaterialChoice(Material.STRIPPED_ACACIA_LOG, Material.STRIPPED_ACACIA_WOOD), 160));
				recipes.add(new SimpleMachineRecipe(this, "jungle_planks", new ItemStack(Material.JUNGLE_PLANKS, 6), new RecipeChoice.MaterialChoice(Material.STRIPPED_JUNGLE_LOG, Material.STRIPPED_JUNGLE_WOOD), 160));
				recipes.add(new SimpleMachineRecipe(this, "mangrove_planks", new ItemStack(Material.MANGROVE_PLANKS, 6), new RecipeChoice.MaterialChoice(Material.STRIPPED_MANGROVE_LOG, Material.STRIPPED_MANGROVE_WOOD), 160));
				recipes.add(new SimpleMachineRecipe(this, "cherry_planks", new ItemStack(Material.CHERRY_PLANKS, 6), new RecipeChoice.MaterialChoice(Material.STRIPPED_CHERRY_LOG, Material.STRIPPED_CHERRY_WOOD), 160));
				recipes.add(new SimpleMachineRecipe(this, "pale_oak_planks", new ItemStack(Material.PALE_OAK_PLANKS, 6), new RecipeChoice.MaterialChoice(Material.STRIPPED_PALE_OAK_LOG, Material.STRIPPED_PALE_OAK_WOOD), 160));
				recipes.add(new SimpleMachineRecipe(this, "bamboo_planks", new ItemStack(Material.BAMBOO_PLANKS, 3), new RecipeChoice.MaterialChoice(Material.STRIPPED_BAMBOO_BLOCK), 160));
				recipes.add(new SimpleMachineRecipe(this, "crimson_planks", new ItemStack(Material.CRIMSON_PLANKS, 6), new RecipeChoice.MaterialChoice(Material.STRIPPED_CRIMSON_STEM, Material.STRIPPED_CRIMSON_HYPHAE), 160));
				recipes.add(new SimpleMachineRecipe(this, "warped_planks", new ItemStack(Material.WARPED_PLANKS, 6), new RecipeChoice.MaterialChoice(Material.STRIPPED_WARPED_STEM, Material.STRIPPED_WARPED_HYPHAE), 160));

				if(this.tier.getTier() > 2) {
					recipes.add(new SimpleMachineRecipe(this, "oak_fences", new ItemStack(Material.OAK_FENCE, 2), new ItemStack(Material.OAK_PLANKS), 240));
					recipes.add(new SimpleMachineRecipe(this, "spruce_fences", new ItemStack(Material.SPRUCE_FENCE, 2), new ItemStack(Material.SPRUCE_PLANKS), 240));
					recipes.add(new SimpleMachineRecipe(this, "birch_fences", new ItemStack(Material.BIRCH_FENCE, 2), new ItemStack(Material.BIRCH_PLANKS), 240));
					recipes.add(new SimpleMachineRecipe(this, "dark_oak_fences", new ItemStack(Material.DARK_OAK_FENCE, 2), new ItemStack(Material.DARK_OAK_PLANKS), 240));
					recipes.add(new SimpleMachineRecipe(this, "acacia_fences", new ItemStack(Material.ACACIA_FENCE, 2), new ItemStack(Material.ACACIA_PLANKS), 240));
					recipes.add(new SimpleMachineRecipe(this, "jungle_fences", new ItemStack(Material.JUNGLE_FENCE, 2), new ItemStack(Material.JUNGLE_PLANKS), 240));
					recipes.add(new SimpleMachineRecipe(this, "mangrove_fences", new ItemStack(Material.MANGROVE_FENCE, 2), new ItemStack(Material.MANGROVE_PLANKS), 240));
					recipes.add(new SimpleMachineRecipe(this, "cherry_fences", new ItemStack(Material.CHERRY_FENCE, 2), new ItemStack(Material.CHERRY_PLANKS), 240));
					recipes.add(new SimpleMachineRecipe(this, "pale_oak_fences", new ItemStack(Material.PALE_OAK_FENCE, 2), new ItemStack(Material.PALE_OAK_PLANKS), 240));
					recipes.add(new SimpleMachineRecipe(this, "bamboo_fences", new ItemStack(Material.BAMBOO_FENCE, 2), new ItemStack(Material.BAMBOO_PLANKS), 240));
					recipes.add(new SimpleMachineRecipe(this, "crimson_fences", new ItemStack(Material.CRIMSON_FENCE, 2), new ItemStack(Material.CRIMSON_PLANKS), 240));
					recipes.add(new SimpleMachineRecipe(this, "warped_fences", new ItemStack(Material.WARPED_FENCE, 2), new ItemStack(Material.WARPED_PLANKS), 240));

					if(this.tier.getTier() > 3) {
						recipes.add(new SimpleMachineRecipe(this, "oak_trapdoors", new ItemStack(Material.OAK_TRAPDOOR, 2), new ItemStack(Material.OAK_SLAB), 240));
						recipes.add(new SimpleMachineRecipe(this, "spruce_trapdoors", new ItemStack(Material.SPRUCE_TRAPDOOR, 2), new ItemStack(Material.SPRUCE_SLAB), 240));
						recipes.add(new SimpleMachineRecipe(this, "birch_trapdoors", new ItemStack(Material.BIRCH_TRAPDOOR, 2), new ItemStack(Material.BIRCH_SLAB), 240));
						recipes.add(new SimpleMachineRecipe(this, "dark_oak_trapdoors", new ItemStack(Material.DARK_OAK_TRAPDOOR, 2), new ItemStack(Material.DARK_OAK_SLAB), 240));
						recipes.add(new SimpleMachineRecipe(this, "acacia_trapdoors", new ItemStack(Material.ACACIA_TRAPDOOR, 2), new ItemStack(Material.ACACIA_SLAB), 240));
						recipes.add(new SimpleMachineRecipe(this, "jungle_trapdoors", new ItemStack(Material.JUNGLE_TRAPDOOR, 2), new ItemStack(Material.JUNGLE_SLAB), 240));
						recipes.add(new SimpleMachineRecipe(this, "mangrove_trapdoors", new ItemStack(Material.MANGROVE_TRAPDOOR, 2), new ItemStack(Material.MANGROVE_SLAB), 240));
						recipes.add(new SimpleMachineRecipe(this, "cherry_trapdoors", new ItemStack(Material.CHERRY_TRAPDOOR, 2), new ItemStack(Material.CHERRY_SLAB), 240));
						recipes.add(new SimpleMachineRecipe(this, "pale_oak_trapdoors", new ItemStack(Material.PALE_OAK_TRAPDOOR, 2), new ItemStack(Material.PALE_OAK_SLAB), 240));
						recipes.add(new SimpleMachineRecipe(this, "bamboo_trapdoors", new ItemStack(Material.BAMBOO_TRAPDOOR, 2), new ItemStack(Material.BAMBOO_SLAB), 240));
						recipes.add(new SimpleMachineRecipe(this, "crimson_trapdoors", new ItemStack(Material.CRIMSON_TRAPDOOR, 2), new ItemStack(Material.CRIMSON_SLAB), 240));
						recipes.add(new SimpleMachineRecipe(this, "warped_trapdoors", new ItemStack(Material.WARPED_TRAPDOOR, 2), new ItemStack(Material.WARPED_SLAB), 240));
					}
				}
			}
		}

		return recipes;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		switch (this.tier) {
			case BASIC:
				return Material.IRON_AXE;
			case ADVANCED:
				return Material.GOLDEN_AXE;
			case IMPROVED:
				return Material.DIAMOND_AXE;
			case PERFECTED:
				return Material.NETHERITE_AXE;
			default:
				return null;
		}
	}


	@Override
	public String getTitle() {
		return this.tier.getName() + " Sawmill";
	}


	@Override
	public String getMachineRegistryName() {
		return "sawmill";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.SAWMILL;
	}

}
