
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
import me.gamma.cookies.objects.recipe.CompressionRecipe;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.MachineRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;



public class CarbonPress extends AbstractSkullMachine {

	public CarbonPress(MachineTier tier) {
		super(tier);
	}


	@Override
	public List<MachineRecipe> getMachineRecipes() {
		List<MachineRecipe> recipes = new ArrayList<>();
		if(tier.getTier() > 0) {
			recipes.add(new CompressionRecipe("coal_to_carbon", CustomBlockSetup.CARBON.createDefaultItemStack(), new ItemStack(Material.COAL, 4), 120));
			recipes.add(new CompressionRecipe("carbon_to_compressed_carbon", CustomBlockSetup.COMPRESSED_CARBON.createDefaultItemStack(), CustomBlockSetup.CARBON.createDefaultItemStack(), 8, 200));

			if(tier.getTier() > 1) {
				recipes.add(new CompressionRecipe("compressed_carbon_to_carbon_chunk", CustomBlockSetup.CARBON_CHUNK.createDefaultItemStack(), CustomBlockSetup.COMPRESSED_CARBON.createDefaultItemStack(), 8, 320));

				if(tier.getTier() > 2) {
					recipes.add(new CompressionRecipe("carbon_chunk_to_carbonado", CustomBlockSetup.CARBONADO.createDefaultItemStack(), CustomBlockSetup.CARBON_CHUNK.createDefaultItemStack(), 4, 480));

					if(tier.getTier() > 3) {
						recipes.add(new CompressionRecipe("carbonado_to_diamond", new ItemStack(Material.DIAMOND), CustomBlockSetup.CARBONADO.createDefaultItemStack(), 640));
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
		return tier.getName() + " §8Carbon Press ";
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
				carbon = new ItemStack(Material.COAL);
				break;
			case ADVANCED:
				center = CustomBlockSetup.CARBON_PRESS.createDefaultItemStack();
				carbon = CustomBlockSetup.COMPRESSED_CARBON.createDefaultItemStack();
				break;
			case IMPROVED:
				center = CustomBlockSetup.ADVANCED_CARBON_PRESS.createDefaultItemStack();
				carbon = CustomBlockSetup.CARBON_CHUNK.createDefaultItemStack();
				break;
			case PERFECTED:
				center = CustomBlockSetup.IMPROVED_CARBON_PRESS.createDefaultItemStack();
				carbon = CustomBlockSetup.CARBONADO.createDefaultItemStack();
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
