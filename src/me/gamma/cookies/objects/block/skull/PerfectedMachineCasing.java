package me.gamma.cookies.objects.block.skull;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;


public class PerfectedMachineCasing extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.PERFECTED_MACHINE_CASING;
	}


	@Override
	public String getIdentifier() {
		return "perfected_machine_casing";
	}


	@Override
	public String getDisplayName() {
		return "§dPerfected Machine Casing";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Only for the Best of all Machines.");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("ENE", "NCN", "ENE");
		recipe.setIngredient('N', Material.NETHERITE_INGOT);
		recipe.setIngredient('E', Material.EMERALD);
		recipe.setIngredient('C', CustomBlockSetup.IMPROVED_MACHINE_CASING.createDefaultItemStack());
		return recipe;
	}

}
