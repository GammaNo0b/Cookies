
package me.gamma.cookies.objects.item.food;


import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.setup.CustomItemSetup;



public class MilkShake extends Drink {

	public MilkShake(String name, String identifier, int hunger, int saturation, ItemStack ingredient, Color color) {
		super(name, identifier, hunger, saturation, ingredient, color);
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = (CustomRecipe) super.getRecipe();
		recipe.setIngredient('B', CustomItemSetup.MILK_BOTTLE.createDefaultItemStack());
		return recipe;
	}

}
