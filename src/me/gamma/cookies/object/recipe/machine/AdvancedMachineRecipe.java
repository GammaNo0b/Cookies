
package me.gamma.cookies.object.recipe.machine;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.persistence.PersistentDataContainer;

import com.google.gson.JsonObject;

import me.gamma.cookies.object.block.machine.AbstractMachine;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.recipe.IngredientsListBuilder;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.JsonUtils;



public class AdvancedMachineRecipe implements MachineRecipe {

	private final AbstractMachine machine;
	private final String identifier;
	private final ItemStack result;
	private final ItemStack[] extra;
	private final int duration;
	private RecipeChoice[] ingredients;

	public AdvancedMachineRecipe(AbstractMachine machine, String identifier, ItemStack result, int duration, ItemStack... extra) {
		this.machine = machine;
		this.identifier = identifier;
		this.result = result;
		this.duration = duration;
		this.extra = extra;
	}


	public AdvancedMachineRecipe setIngredients(ItemStack... ingredients) {
		this.ingredients = ArrayUtils.map(ingredients, RecipeChoice.ExactChoice::new, RecipeChoice[]::new);
		return this;
	}


	public AdvancedMachineRecipe setIngredients(Material... ingredients) {
		this.ingredients = ArrayUtils.map(ingredients, RecipeChoice.MaterialChoice::new, RecipeChoice[]::new);
		return this;
	}


	public AdvancedMachineRecipe setIngredients(RecipeChoice... ingredients) {
		this.ingredients = ingredients;
		return this;
	}


	public IngredientsListBuilder buildIngredients() {
		return new IngredientsListBuilder(l -> this.setIngredients(l.toArray(RecipeChoice[]::new)));
	}


	@Override
	public ItemStack getResult() {
		return this.result;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public AbstractMachine getMachine() {
		return this.machine;
	}


	@Override
	public int getDuration() {
		return this.duration;
	}


	@Override
	public RecipeChoice[] getIngredients() {
		return this.ingredients;
	}


	@Override
	public ItemStack[] getExtraResults() {
		return this.extra;
	}


	@Override
	public ItemStack createIcon(int cycle) {
		return this.result;
	}

	private static final IntegerProperty DURATION = new IntegerProperty("duration");

	public void store(PersistentDataContainer container) {
		DURATION.store(container, this.duration);
	}


	@SuppressWarnings("deprecation")
	@Override
	public JsonObject saveRecipe() {
		JsonObject object = new JsonObject();
		object.addProperty("name", this.getIdentifier());
		object.add("result", JsonUtils.convertItemStack(this.result));
		object.addProperty("duration", this.duration);
		object.add("extra", JsonUtils.convertItemStacks(this.extra));
		object.add("ingredients", JsonUtils.convertItemStacks(ArrayUtils.map(this.ingredients, RecipeChoice::getItemStack, ItemStack[]::new)));
		return object;
	}


	/**
	 * Reads and returns a advanced machine recipe from the given json object and given machine.
	 * 
	 * @param object  the json object
	 * @param machine the machine
	 * @return the read recipe
	 */
	public static AdvancedMachineRecipe loadRecipe(JsonObject object, AbstractMachine machine) {
		String identifier = object.get("name").getAsString();
		ItemStack result = JsonUtils.parseItemStack(object.get("result").getAsJsonObject());
		int duration = object.get("duration").getAsInt();
		ItemStack[] extra = JsonUtils.parseItemStacks(object.get("extra"));
		ItemStack[] ingredients = JsonUtils.parseItemStacks(object.get("ingredients"));
		AdvancedMachineRecipe recipe = new AdvancedMachineRecipe(machine, identifier, result, duration, extra);
		recipe.setIngredients(ingredients);
		return recipe;
	}

}
