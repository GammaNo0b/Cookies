
package me.gamma.cookies.objects.block.skull.machine;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.machine.MachineTier;
import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.list.TieredMaterials;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;



public class MachineCasing extends AbstractSkullBlock {

	private MachineTier tier;

	public MachineCasing(MachineTier tier) {
		this.tier = tier;
	}


	public MachineTier getTier() {
		return tier;
	}


	@Override
	public String getBlockTexture() {
		switch(tier) {
			case BASIC:
				return HeadTextures.MACHINE_CASING;
			case ADVANCED:
				return HeadTextures.ADVANCED_MACHINE_CASING;
			case IMPROVED:
				return HeadTextures.IMPROVED_MACHINE_CASING;
			case PERFECTED:
				return HeadTextures.PERFECTED_MACHINE_CASING;
			default:
				return "";
		}
	}


	@Override
	public String getDisplayName() {
		return this.tier.getName() + " §9Machine §3Casing";
	}


	@Override
	public String getRegistryName() {
		return "machine_casing_" + tier.name().toLowerCase();
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Heart of many Machines!");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("GIG", "ICI", "GIG");
		ItemStack core = new ItemStack(Material.REDSTONE_BLOCK);
		if(tier != MachineTier.BASIC) {
			core = getByTier(tier.decrease()).createDefaultItemStack();
		}
		recipe.setIngredient('G', TieredMaterials.getGem(tier));
		recipe.setIngredient('I', TieredMaterials.getIngot(tier));
		recipe.setIngredient('C', core);
		return recipe;
	}


	public static MachineCasing getByTier(MachineTier tier) {
		switch (tier) {
			case BASIC:
				return CustomBlockSetup.MACHINE_CASING;
			case ADVANCED:
				return CustomBlockSetup.ADVANCED_MACHINE_CASING;
			case IMPROVED:
				return CustomBlockSetup.IMPROVED_MACHINE_CASING;
			case PERFECTED:
				return CustomBlockSetup.PERFECTED_MACHINE_CASING;
			default:
				return null;
		}
	}

}
