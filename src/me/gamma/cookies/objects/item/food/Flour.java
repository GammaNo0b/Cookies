package me.gamma.cookies.objects.item.food;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractSkullItem;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class Flour extends AbstractSkullItem {

	@Override
	protected String getBlockTexture() {
		return HeadTextures.FLOUR;
	}

	@Override
	public String getRegistryName() {
		return "flour";
	}

	@Override
	public String getDisplayName() {
		return "§eFlour";
	}

	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.KITCHEN_INGREDIENTS, RecipeType.KITCHEN);
		recipe.setShape("W");
		recipe.setIngredient('W', Material.WHEAT);
		return recipe;
	}

}
