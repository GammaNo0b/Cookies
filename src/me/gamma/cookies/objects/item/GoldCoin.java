
package me.gamma.cookies.objects.item;


import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.util.ItemBuilder;



public class GoldCoin extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
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
		Recipe recipe = new ShapelessRecipe(new NamespacedKey(Cookies.INSTANCE, "gold_coin"), new ItemBuilder(this.createDefaultItemStack()).setAmount(9).build()).addIngredient(Material.GOLD_NUGGET);
		RecipeCategory.RESOURCES.registerRecipe(recipe);
		return recipe;
	}

}
