
package me.gamma.cookies.objects.block.skull;


import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class CompressedCobblestone extends CustomSkullBlock {

	private final int stage;
	private final ItemStack previous;

	public CompressedCobblestone(String identifier, String name, Supplier<RecipeCategory> category, String texture, int stage, ItemStack previous) {
		super(identifier, name, category, texture);
		this.stage = stage;
		this.previous = previous;
	}


	@Override
	public List<String> getDescription() {
		List<String> descritpion = super.getDescription();
		descritpion.add(String.format("§7Contains %d Cobblestone!", (int) Math.pow(9, this.stage)));
		return descritpion;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.RESOURCES, RecipeType.CUSTOM);
		recipe.setShape("CCC", "CCC", "CCC");
		recipe.setIngredient('C', this.previous);
		return recipe;
	}


	public Recipe getUncompressionRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.previous, 9, RecipeCategory.RESOURCES, RecipeType.CUSTOM);
		recipe.setShape("C");
		recipe.setIngredient('C', this.createDefaultItemStack());
		return recipe;
	}


	@Override
	public List<Recipe> getRecipes() {
		return Arrays.asList(this.getRecipe(), this.getUncompressionRecipe());
	}

}
