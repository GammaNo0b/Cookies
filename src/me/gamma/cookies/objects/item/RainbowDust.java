package me.gamma.cookies.objects.item;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.Utilities;

public class RainbowDust extends Dust {

	public RainbowDust() {
		super("rainbow_dust", Utilities.colorize("Rainbow Dust", "4c6ea23915".toCharArray(), 1), Material.GLOWSTONE_DUST, CustomModelDataValues.RAINBOW_DUST);
	}
	
	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.RESOURCES, RecipeType.CUSTOM);
		recipe.setShape("187", "2G6", "345");
		recipe.setIngredient('1', Material.RED_DYE);
		recipe.setIngredient('2', Material.ORANGE_DYE);
		recipe.setIngredient('3', Material.YELLOW_DYE);
		recipe.setIngredient('4', Material.LIME_DYE);
		recipe.setIngredient('5', Material.GREEN_DYE);
		recipe.setIngredient('6', Material.CYAN_DYE);
		recipe.setIngredient('7', Material.BLUE_DYE);
		recipe.setIngredient('8', Material.PURPLE_DYE);
		recipe.setIngredient('G', Material.GLOWSTONE_DUST);
		return recipe;
	}

}
