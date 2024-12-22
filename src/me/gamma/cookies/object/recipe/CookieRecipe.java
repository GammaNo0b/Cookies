
package me.gamma.cookies.object.recipe;


import org.bukkit.inventory.Recipe;

import com.google.gson.JsonObject;



/**
 * The super class of Custom Recipes in the Cookie Plugin.
 * 
 * @author gamma
 *
 */
public interface CookieRecipe extends Recipe {

	/**
	 * Returns the {@link RecipeType} of this recipe.
	 * 
	 * @return the type
	 */
	RecipeType getType();


	/**
	 * Reads and returns a recipe from the given json object.
	 * 
	 * @param object the json object
	 * @return the read recipe
	 */
	static Recipe loadRecipe(JsonObject object) {
		RecipeType type = RecipeType.byId(object.get("type").getAsString());
		if(type == null)
			return null;

		if(type == RecipeType.MACHINE) {
			return null; // MachineRecipe.loadRecipe(object);
		} else {
			return CustomRecipe.loadRecipe(object, type);
		}
	}

}
