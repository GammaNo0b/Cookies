package me.gamma.cookies.objects.block.skull;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class MachineCasing extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.MACHINE_CASING;
	}


	@Override
	public String getDisplayName() {
		return "§9Machine §3Casing";
	}


	@Override
	public String getIdentifier() {
		return "machine_casing";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Heart of many Machines!");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("NIN", "IRI", "NIN");
		recipe.setIngredient('N', Material.IRON_NUGGET);
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('R', Material.REDSTONE_BLOCK);
		return recipe;
	}

}
