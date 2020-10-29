package me.gamma.cookies.objects.item;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class Hamburger extends AbstractFood {

	@Override
	public int getHunger() {
		return 8;
	}


	@Override
	public int getSaturation() {
		return 6;
	}


	@Override
	protected Material getFood() {
		return Material.BREAD;
	}


	@Override
	public String getIdentifier() {
		return "hamburger";
	}


	@Override
	public String getDisplayName() {
		return "§cHamburger";
	}
	
	
	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.HAMBURGER;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.FOOD, RecipeType.KITCHEN);
		recipe.setShape("BS");
		recipe.setIngredient('B', Material.BREAD);
		recipe.setIngredient('S', Material.COOKED_BEEF);
		return recipe;
	}

}
