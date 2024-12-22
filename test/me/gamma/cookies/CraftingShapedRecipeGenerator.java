
package me.gamma.cookies;


import org.bukkit.inventory.ShapedRecipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;



public class CraftingShapedRecipeGenerator implements RecipeGenerator<ShapedRecipe> {

	@Override
	public String getType() {
		return "minecraft:crafting_shaped";
	}


	@Override
	public JsonObject generate(ShapedRecipe resource) {
		JsonObject object = RecipeGenerator.super.generate(resource);
		object.add("result", RecipeGenerator.itemstack(resource.getResult()));
		object.addProperty("category", resource.getCategory().name().toLowerCase());
		if(!resource.getGroup().isEmpty())
			object.addProperty("group", resource.getGroup());
		String[] shape = resource.getShape();
		JsonArray pattern = new JsonArray(shape.length);
		for(int i = 0; i < shape.length; i++)
			pattern.add(shape[i]);
		object.add("pattern", pattern);
		JsonObject key = new JsonObject();
		object.add("key", key);
		return object;
	}

}
