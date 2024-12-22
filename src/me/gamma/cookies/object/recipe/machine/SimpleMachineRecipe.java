
package me.gamma.cookies.object.recipe.machine;


import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import com.google.gson.JsonObject;

import me.gamma.cookies.object.block.machine.AbstractMachine;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.JsonUtils;



public class SimpleMachineRecipe implements MachineRecipe {

	private final AbstractMachine machine;
	private final String identifier;
	private final ItemStack result;
	private final RecipeChoice ingredient;
	private final int duration;

	public SimpleMachineRecipe(AbstractMachine machine, String identifier, ItemStack result, ItemStack ingredient, int duration) {
		this(machine, identifier, result, new RecipeChoice.ExactChoice(ingredient), duration);
	}


	public SimpleMachineRecipe(AbstractMachine machine, String identifier, ItemStack result, int amount, ItemStack ingredient, int duration) {
		this(machine, identifier, new ItemBuilder(result).setAmount(amount).build(), ingredient, duration);
	}


	public SimpleMachineRecipe(AbstractMachine machine, String identifier, ItemStack result, ItemStack ingredient, int amount, int duration) {
		this(machine, identifier, result, new RecipeChoice.ExactChoice(new ItemBuilder(ingredient).setAmount(amount).build()), duration);
	}


	public SimpleMachineRecipe(AbstractMachine machine, String identifier, ItemStack result, RecipeChoice ingredient, int duration) {
		this.machine = machine;
		this.identifier = identifier;
		this.result = result;
		this.ingredient = ingredient;
		this.duration = duration;
	}


	public SimpleMachineRecipe(AbstractMachine machine, String identifier, ItemStack result, int amount, RecipeChoice ingredient, int duration) {
		this(machine, identifier, new ItemBuilder(result).setAmount(amount).build(), ingredient, duration);
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
	public ItemStack getResult() {
		return result;
	}


	@Override
	public ItemStack[] getExtraResults() {
		return new ItemStack[0];
	}


	@Override
	public int getDuration() {
		return this.duration;
	}


	@Override
	public RecipeChoice[] getIngredients() {
		return new RecipeChoice[] { this.ingredient };
	}


	@Override
	public ItemStack createIcon(int cycle) {
		return this.result;
	}


	@SuppressWarnings("deprecation")
	@Override
	public JsonObject saveRecipe() {
		JsonObject object = new JsonObject();
		object.addProperty("name", this.getIdentifier());
		object.add("result", JsonUtils.convertItemStack(this.getResult()));
		object.add("ingredient", JsonUtils.convertItemStack(this.ingredient.getItemStack()));
		return object;
	}


	/**
	 * Reads and returns a simple machine recipe from the given json object and given machine.
	 * 
	 * @param object  the json object
	 * @param machine the machine
	 * @return the read recipe
	 */
	public static SimpleMachineRecipe loadRecipe(JsonObject object, AbstractMachine machine) {
		String identifier = object.get("name").getAsString();
		ItemStack result = JsonUtils.parseItemStack(object.get("result").getAsJsonObject());
		ItemStack ingredient = JsonUtils.parseItemStack(object.get("ingredient").getAsJsonObject());
		int duration = object.get("duration").getAsInt();
		return new SimpleMachineRecipe(machine, identifier, result, ingredient, duration);
	}

}
