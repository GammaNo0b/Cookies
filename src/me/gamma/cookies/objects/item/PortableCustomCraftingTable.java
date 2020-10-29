package me.gamma.cookies.objects.item;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;


public class PortableCustomCraftingTable extends PortableCustomCraftingOpener {

	@Override
	protected RecipeType getType() {
		return RecipeType.CUSTOM;
	}


	@Override
	protected String getBlockTexture() {
		return HeadTextures.FLETCHING_TABLE;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MISCELLANEOUS, RecipeType.CUSTOM);
		recipe.setShape(" R ", "ITG", " E ");
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('I', Material.IRON_NUGGET);
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('E', Material.EMERALD);
		recipe.setIngredient('T', CustomItemSetup.PORTABLE_CRAFTING_TABLE.createDefaultItemStack());
		return recipe;
	}

}
