
package me.gamma.cookies.objects.item.food;


import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractSkullItem;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;



public class Dough extends AbstractSkullItem {

	@Override
	protected String getBlockTexture() {
		return HeadTextures.DOUGH;
	}


	@Override
	public String getRegistryName() {
		return "dough";
	}


	@Override
	public String getDisplayName() {
		return "§6Dough";
	}


	@Override
	public Material getMaterial() {
		return Material.PLAYER_HEAD;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.KITCHEN_INGREDIENTS, RecipeType.KITCHEN);
		recipe.setShape("FFS");
		recipe.setIngredient('F', CustomItemSetup.FLOUR.createDefaultItemStack());
		recipe.setIngredient('S', Material.SUGAR);
		return recipe;
	}

}
