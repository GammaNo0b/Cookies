package me.gamma.cookies.objects.item;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;

public class Cheeseburger extends AbstractFood {

	@Override
	public int getHunger() {
		return 12;
	}

	@Override
	public int getSaturation() {
		return 10;
	}

	@Override
	protected Material getFood() {
		return Material.BREAD;
	}

	@Override
	public String getIdentifier() {
		return "cheeseburger";
	}

	@Override
	public String getDisplayName() {
		return "§eCheeseburger";
	}
	
	
	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.CHEESEBURGER;
	}

	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.FOOD, RecipeType.KITCHEN);
		recipe.setShape("HC");
		recipe.setIngredient('H', CustomItemSetup.HAMBURGER.createDefaultItemStack());
		recipe.setIngredient('C', CustomItemSetup.CHEESE.createDefaultItemStack());
		return recipe;
	}

}
