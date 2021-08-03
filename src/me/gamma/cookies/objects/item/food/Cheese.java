package me.gamma.cookies.objects.item.food;

import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractSkullItem;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;


public class Cheese extends AbstractSkullItem {

	@Override
	protected String getBlockTexture() {
		return HeadTextures.CHEESE;
	}


	@Override
	public String getRegistryName() {
		return "cheese";
	}


	@Override
	public String getDisplayName() {
		return "§eCheese";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.KITCHEN_INGREDIENTS, RecipeType.KITCHEN);
		recipe.setShape("MS");
		recipe.setIngredient('M', CustomItemSetup.MILK_BOTTLE.createDefaultItemStack());
		recipe.setIngredient('S', CustomItemSetup.SALT.createDefaultItemStack());
		return recipe;
	}

}
