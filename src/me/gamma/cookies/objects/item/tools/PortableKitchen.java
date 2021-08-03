
package me.gamma.cookies.objects.item.tools;


import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;



public class PortableKitchen extends PortableCustomCraftingOpener {

	@Override
	protected RecipeType getType() {
		return RecipeType.KITCHEN;
	}


	@Override
	protected String getBlockTexture() {
		return HeadTextures.SMOKER_OFF;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MISCELLANEOUS, RecipeType.CUSTOM);
		recipe.setShape("BBB", "SFA", "MCH");
		recipe.setIngredient('B', Material.BRICK);
		recipe.setIngredient('S', Material.STONE_PRESSURE_PLATE);
		recipe.setIngredient('F', Material.FLOWER_POT);
		recipe.setIngredient('A', Material.BARREL);
		recipe.setIngredient('M', Material.SMOKER);
		recipe.setIngredient('C', CustomItemSetup.PORTABLE_CUSTOM_CRAFTING_TABLE.createDefaultItemStack());
		recipe.setIngredient('H', Material.BOOKSHELF);
		return recipe;
	}

}
