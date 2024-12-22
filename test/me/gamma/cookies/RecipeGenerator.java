
package me.gamma.cookies;


import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import me.gamma.cookies.util.JsonUtils;



public interface RecipeGenerator<R extends Recipe> extends ResourceGenerator<R> {

	String getType();


	@Override
	default JsonObject generate(R resource) {
		JsonObject object = new JsonObject();
		object.addProperty("type", this.getType());
		return object;
	}


	static JsonObject itemstack(ItemStack stack) {
		return JsonUtils.convertItemStack(stack);
	}


	static JsonArray itemstacks(ItemStack... stacks) {
		return JsonUtils.convertItemStacks(stacks);
	}

}
