
package me.gamma.cookies.object.recipe;


import java.util.function.BiPredicate;

import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.RecipeUtils;



public enum RecipeShape {

	SHAPED(RecipeUtils::matchShaped),
	SHAPELESS(RecipeUtils::matchShapeless),
	EXACTLY(RecipeUtils::matchExactly);

	private BiPredicate<CustomRecipe, ItemStack[][]> validator;

	private RecipeShape(BiPredicate<CustomRecipe, ItemStack[][]> validator) {
		this.validator = validator;
	}


	public boolean validateRecipe(CustomRecipe recipe, ItemStack[][] matrix) {
		return this.validator.test(recipe, matrix);
	}


	public static RecipeShape byId(String id) {
		for(RecipeShape shape : RecipeShape.values())
			if(shape.name().toLowerCase().equals(id))
				return shape;
		return null;
	}

}
