
package me.gamma.cookies.objects.block.skull.machine;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.MachineTier;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.list.TieredMaterials;
import me.gamma.cookies.objects.recipe.CompressionRecipe;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.MachineRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;



public class Compressor extends AbstractSkullMachine {

	private MachineTier tier;

	public Compressor(MachineTier tier) {
		this.tier = tier;
	}


	@Override
	public MachineTier getTier() {
		return tier;
	}


	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>();

		if(tier.getTier() > 0) {
			recipes.add(new CompressionRecipe("wheat_to_hay_block", new ItemStack(Material.HAY_BLOCK), new ItemStack(Material.WHEAT, 9), 60));
			recipes.add(new CompressionRecipe("melons_to_block", new ItemStack(Material.MELON), new ItemStack(Material.MELON_SLICE, 9), 80));
			recipes.add(new CompressionRecipe("dried_kelp_to_block", new ItemStack(Material.DRIED_KELP_BLOCK), new ItemStack(Material.DRIED_KELP, 9), 60));
			recipes.add(new CompressionRecipe("clay_to_clay_block", new ItemStack(Material.CLAY), new ItemStack(Material.CLAY, 4), 60));
			recipes.add(new CompressionRecipe("string_to_wool", new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.STRING, 4), 80));
			recipes.add(new CompressionRecipe("glowstone_to_block", new ItemStack(Material.GLOWSTONE), new ItemStack(Material.GLOWSTONE_DUST, 4), 80));
			recipes.add(new CompressionRecipe("nether_warts_to_block", new ItemStack(Material.NETHER_WART_BLOCK), new ItemStack(Material.NETHER_WART, 9), 80));

			if(tier.getTier() > 1) {
				recipes.add(new CompressionRecipe("coal_to_coal_block", new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL, 9), 120));
				recipes.add(new CompressionRecipe("iron_nugget_to_iron_ingot", new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_NUGGET, 9), 120));
				recipes.add(new CompressionRecipe("iron_to_iron_block", new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_INGOT, 9), 120));
				recipes.add(new CompressionRecipe("gold_nugget_to_gold_ingot", new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.GOLD_NUGGET, 9), 120));
				recipes.add(new CompressionRecipe("gold_to_gold_block", new ItemStack(Material.GOLD_BLOCK), new ItemStack(Material.GOLD_INGOT, 9), 120));
				recipes.add(new CompressionRecipe("redstone_to_redstone_block", new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.REDSTONE, 9), 120));
				recipes.add(new CompressionRecipe("lapis_to_lapis_block", new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.LAPIS_LAZULI, 9), 120));
				recipes.add(new CompressionRecipe("emerald_to_emerald_block", new ItemStack(Material.EMERALD_BLOCK), new ItemStack(Material.EMERALD, 9), 120));
				recipes.add(new CompressionRecipe("diamond_to_diamond_block", new ItemStack(Material.DIAMOND_BLOCK), new ItemStack(Material.DIAMOND, 9), 120));
				recipes.add(new CompressionRecipe("quartz_to_quartz_block", new ItemStack(Material.QUARTZ_BLOCK), new ItemStack(Material.QUARTZ, 4), 120));
				recipes.add(new CompressionRecipe("netherite_to_netherite_block", new ItemStack(Material.NETHERITE_BLOCK), new ItemStack(Material.NETHERITE_INGOT, 9), 120));
			}
		}

		return recipes;
	}


	@Override
	public ItemStack getProgressIcon() {
		return new ItemStack(Material.PISTON);
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.COMPRESSOR;
	}


	@Override
	public String getMachineIdentifier() {
		return "compressor";
	}


	@Override
	public String getDisplayName() {
		return "§eCompressor";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Compresses Materials to create Resources with higher density.");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" S ", "PTP", "MCM");
		ItemStack center = null;
		switch (this.tier) {
			case BASIC:
				center = new ItemStack(Material.IRON_TRAPDOOR);
				break;
			case ADVANCED:
				center = CustomBlockSetup.COMPRESSOR.createDefaultItemStack();
				break;
			case IMPROVED:
				center = CustomBlockSetup.ADVANCED_COMPRESSOR.createDefaultItemStack();
				break;
			case PERFECTED:
				center = CustomBlockSetup.IMPROVED_COMPRESSOR.createDefaultItemStack();
				break;
			default:
				break;
		}
		recipe.setIngredient('S', TieredMaterials.getIngot(tier));
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('T', center);
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('C', TieredMaterials.getCore(tier));
		return recipe;
	}

}
