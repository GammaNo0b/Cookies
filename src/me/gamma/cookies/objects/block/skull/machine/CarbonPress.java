
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
import me.gamma.cookies.setup.CustomItemSetup;



public class CarbonPress extends AbstractSkullMachine {

	private MachineTier tier;

	public CarbonPress(MachineTier tier) {
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
			recipes.add(new CompressionRecipe("coal_to_carbon", CustomItemSetup.CARBON.createDefaultItemStack(), new ItemStack(Material.COAL, 4), 120));
			recipes.add(new CompressionRecipe("carbon_to_compressed_carbon", CustomItemSetup.COMPRESSED_CARBON.createDefaultItemStack(), CustomItemSetup.CARBON.createDefaultItemStack(), 8, 200));

			if(tier.getTier() > 1) {
				recipes.add(new CompressionRecipe("compressed_carbon_to_carbon_chunk", CustomItemSetup.CARBON_CHUNK.createDefaultItemStack(), CustomItemSetup.COMPRESSED_CARBON.createDefaultItemStack(), 8, 320));

				if(tier.getTier() > 2) {
					recipes.add(new CompressionRecipe("carbon_chunk_to_carbonado", CustomItemSetup.CARBONADO.createDefaultItemStack(), CustomItemSetup.CARBON_CHUNK.createDefaultItemStack(), 4, 480));

					if(tier.getTier() > 3) {
						recipes.add(new CompressionRecipe("carbonado_to_diamond", new ItemStack(Material.DIAMOND), CustomItemSetup.CARBONADO.createDefaultItemStack(), 640));

					}
				}
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
		return HeadTextures.CARBON_PRESS;
	}


	@Override
	public String getMachineIdentifier() {
		return "carbon_press";
	}


	@Override
	public String getDisplayName() {
		return "§8Carbon Press";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Special Compression Machine that only compresses carbon related resources.");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" S ", "MTM", "KCK");
		ItemStack center = null;
		ItemStack carbon = null;
		switch (this.tier) {
			case BASIC:
				center = new ItemStack(Material.IRON_TRAPDOOR);
				carbon = CustomItemSetup.CARBON.createDefaultItemStack();
				break;
			case ADVANCED:
				center = CustomBlockSetup.CARBON_PRESS.createDefaultItemStack();
				carbon = CustomItemSetup.COMPRESSED_CARBON.createDefaultItemStack();
				break;
			case IMPROVED:
				center = CustomBlockSetup.ADVANCED_CARBON_PRESS.createDefaultItemStack();
				carbon = CustomItemSetup.CARBON_CHUNK.createDefaultItemStack();
				break;
			case PERFECTED:
				center = CustomBlockSetup.IMPROVED_CARBON_PRESS.createDefaultItemStack();
				carbon = CustomItemSetup.CARBONADO.createDefaultItemStack();
				break;
			default:
				break;
		}
		recipe.setIngredient('S', TieredMaterials.getIngot(tier));
		recipe.setIngredient('M', CustomBlockSetup.MOTOR.createDefaultItemStack());
		recipe.setIngredient('T', center);
		recipe.setIngredient('C', TieredMaterials.getCore(tier));
		recipe.setIngredient('K', carbon);
		return recipe;
	}

}
