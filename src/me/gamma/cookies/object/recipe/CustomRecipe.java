
package me.gamma.cookies.object.recipe;


import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.JsonUtils;



public class CustomRecipe implements CookieRecipe {

	private ItemStack result;
	private String[] shape;
	private Map<Character, RecipeChoice> ingredientMap;
	private RecipeType type;
	private RecipeShape recipeshape;
	private ResultProducer producer = ResultProducer.IDENTITY;

	public CustomRecipe(ItemStack result, RecipeType type) {
		this(result, 1, type);
	}


	public CustomRecipe(ItemStack result, RecipeType type, RecipeShape recipeshape) {
		this(result, 1, type, RecipeShape.SHAPED);
	}


	public CustomRecipe(ItemStack result, int amount, RecipeType type) {
		this(result, amount, type, RecipeShape.SHAPED);
	}


	public CustomRecipe(ItemStack result, int amount, RecipeType type, RecipeShape recipeshape) {
		this.result = new ItemBuilder(result).setAmount(amount).build();
		this.ingredientMap = new HashMap<>();
		this.type = type;
		this.recipeshape = recipeshape;
	}


	@Override
	public ItemStack getResult() {
		return this.result;
	}


	public ItemStack getResult(ItemStack[][] ingredients) {
		return this.producer.produceCraftingResult(this.getResult().clone(), ingredients);
	}


	@Override
	public RecipeType getType() {
		return this.type;
	}


	public CustomRecipe setShape(String... shape) {
		this.shape = shape;
		return this;
	}


	public CustomRecipe setIngredient(char key, Material ingredient) {
		return this.setIngredient(key, new CustomMaterialChoice(ingredient));
	}


	public CustomRecipe setIngredient(char key, ItemStack ingredient) {
		ingredient = ingredient.clone();
		ingredient.setAmount(1);
		return this.setIngredient(key, new RecipeChoice.ExactChoice(ingredient));
	}


	public CustomRecipe setIngredient(char key, AbstractCustomItem ingredient) {
		return this.setIngredient(key, new CustomRecipeChoice(ingredient));
	}


	public CustomRecipe setIngredientExact(char key, IItemSupplier ingredient) {
		return this.setIngredient(key, ingredient.get());
	}


	public CustomRecipe setIngredient(char key, Tag<Material> tag) {
		return this.setIngredient(key, new RecipeChoice.MaterialChoice(tag));
	}


	public CustomRecipe setIngredient(char key, RecipeChoice ingredient) {
		this.ingredientMap.put(key, ingredient);
		return this;
	}


	public CustomRecipe setResultProducer(ResultProducer producer) {
		this.producer = producer;
		return this;
	}


	public String[] getShape() {
		return this.shape.clone();
	}


	public Map<Character, RecipeChoice> getIngredientMap() {
		return this.ingredientMap;
	}


	public boolean matches(ItemStack[][] items) {
		return this.recipeshape.validateRecipe(this, items);
	}


	public int getColumns() {
		return this.type.getWidth();
	}


	public int getRows() {
		return this.type.getHeight();
	}


	/**
	 * Reads and returns a custom recipe fronmthe given json object.
	 * 
	 * @param object the json object
	 * @return the read recipe
	 */
	public static CustomRecipe loadRecipe(JsonObject object, RecipeType type) {
		RecipeShape shape = RecipeShape.byId(object.get("shape").getAsString());
		ItemStack result = JsonUtils.parseItemStack(object.get("result").getAsJsonObject());
		CustomRecipe recipe = new CustomRecipe(result, type, shape == null ? RecipeShape.SHAPED : shape);
		JsonArray patternarray = object.get("pattern").getAsJsonArray();
		String[] pattern = new String[patternarray.size()];
		for(int i = 0; i < pattern.length; i++)
			pattern[i] = patternarray.get(i).getAsString();
		recipe.setShape(pattern);
		JsonObject keys = object.get("key").getAsJsonObject();
		for(Map.Entry<String, JsonElement> entry : keys.entrySet()) {
			String key = entry.getKey();
			JsonObject value = entry.getValue().getAsJsonObject();
			recipe.setIngredient(key.charAt(0), JsonUtils.parseItemStack(value));
		}
		return recipe;
	}

	/**
	 * Interface to modify the crafting result using the ingredients.
	 * 
	 * @author gamma
	 *
	 */
	@FunctionalInterface
	public static interface ResultProducer {

		/**
		 * Simply returns the result without modification.
		 */
		ResultProducer IDENTITY = (result, ingredients) -> result;

		/**
		 * Modifies the recipe result using the ingredients.
		 * 
		 * @param result      the recipe result
		 * @param ingredients the ingredients of the crafting recipe
		 * @return the modified result
		 */
		ItemStack produceCraftingResult(ItemStack result, ItemStack[][] ingredients);

	}

}
