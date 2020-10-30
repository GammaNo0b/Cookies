
package me.gamma.cookies.objects.item;


import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.ShowcaseRecipe;



public class SilverCoin extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "silver_coin";
	}


	@Override
	public String getDisplayName() {
		return "§7Silver Coin";
	}


	@Override
	public Material getMaterial() {
		return Material.IRON_NUGGET;
	}


	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.SILVER_COIN;
	}


	@Override
	public Recipe getRecipe() {
		return new ShowcaseRecipe(this.createDefaultItemStack(), RecipeCategory.RESOURCES);
	}

}
