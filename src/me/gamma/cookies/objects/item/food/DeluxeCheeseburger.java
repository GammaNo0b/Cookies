package me.gamma.cookies.objects.item.food;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;


public class DeluxeCheeseburger extends AbstractFood {

	@Override
	public int getHunger() {
		return 16;
	}


	@Override
	public int getSaturation() {
		return 12;
	}


	@Override
	protected Material getFood() {
		return Material.BREAD;
	}


	@Override
	public String getRegistryName() {
		return "deluxe_cheeseburger";
	}


	@Override
	public String getDisplayName() {
		return "§aDeluxe Cheeseburger";
	}
	
	
	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.DELUXE_CHEESEBURGER;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.FOOD, RecipeType.KITCHEN);
		recipe.setShape("CLT");
		recipe.setIngredient('C', CustomItemSetup.CHEESEBURGER.createDefaultItemStack());
		recipe.setIngredient('L', CustomItemSetup.LETTUCE.createDefaultItemStack());
		recipe.setIngredient('T', Material.BEETROOT);
		return recipe;
	}

}
