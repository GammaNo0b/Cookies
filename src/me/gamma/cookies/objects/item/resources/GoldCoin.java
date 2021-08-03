
package me.gamma.cookies.objects.item.resources;


import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.ShowcaseRecipe;



public class GoldCoin extends AbstractCustomItem {

	@Override
	public String getRegistryName() {
		return "gold_coin";
	}


	@Override
	public String getDisplayName() {
		return "§6Gold Coin";
	}


	@Override
	public Material getMaterial() {
		return Material.GOLD_NUGGET;
	}


	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.GOLD_COIN;
	}


	@Override
	public Recipe getRecipe() {
		return new ShowcaseRecipe(this.createDefaultItemStack(), RecipeCategory.RESOURCES);
	}

}
