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


public class AdvancedMachineCasing extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.ADVANCED_MACHINE_CASING;
	}


	@Override
	public String getDisplayName() {
		return "§2Advanced §aMachine §eCasing";
	}


	@Override
	public String getIdentifier() {
		return "advanced_machine_casing";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Core for advanced Machines.");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("NGN", "GCG", "NGN");
		recipe.setIngredient('N', Material.GOLD_NUGGET);
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('C', CustomBlockSetup.MACHINE_CASING.createDefaultItemStack());
		return recipe;
	}

}
