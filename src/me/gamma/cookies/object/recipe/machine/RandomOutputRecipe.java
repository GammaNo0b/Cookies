
package me.gamma.cookies.object.recipe.machine;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.object.block.machine.AbstractMachine;
import me.gamma.cookies.object.gui.task.RecipeInventoryTask;
import me.gamma.cookies.object.gui.task.RecipeInventoryTask.ResultChoice;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.JsonUtils;
import me.gamma.cookies.util.Utils;



public class RandomOutputRecipe implements MachineRecipe {

	private static final DecimalFormat DURATION = new DecimalFormat("0.0s");

	private final AbstractMachine machine;
	private final Random r;
	private int maxChance;
	private final Map<ItemStack, Integer> outputs = new HashMap<>();
	private final RecipeChoice ingredient;
	private final String identifier;
	private final int duration;

	public RandomOutputRecipe(AbstractMachine machine, String identifier, ItemStack ingredient, int duration) {
		this(machine, identifier, ingredient, duration, new Random());
	}


	public RandomOutputRecipe(AbstractMachine machine, String identifier, ItemStack ingredient, int duration, long seed) {
		this(machine, identifier, ingredient, duration, new Random(seed));
	}


	public RandomOutputRecipe(AbstractMachine machine, String identifier, ItemStack ingredient, int duration, Random random) {
		this.machine = machine;
		this.identifier = identifier;
		this.duration = duration;
		this.ingredient = new RecipeChoice.ExactChoice(ingredient);
		this.r = random;
		this.maxChance = 0;
	}


	public RandomOutputRecipe addOutput(Material output, int chance) {
		return this.addOutput(new ItemStack(output), chance);
	}


	public RandomOutputRecipe addOutput(ItemStack output, int chance) {
		this.outputs.put(output, chance);
		this.maxChance += chance;
		return this;
	}


	public RandomOutputRecipe addOutput(IItemSupplier supplier, int chance) {
		return this.addOutput(supplier.get(), chance);
	}


	@Override
	public ItemStack getResult() {
		int n = this.r.nextInt(maxChance);
		int currentChance = 0;
		for(ItemStack stack : outputs.keySet()) {
			int chance = outputs.get(stack);
			if(currentChance <= n && n < currentChance + chance) {
				return stack;
			}
			currentChance += chance;
		}
		return null;
	}


	@Override
	public String getIdentifier() {
		return identifier;
	}


	@Override
	public AbstractMachine getMachine() {
		return this.machine;
	}


	@Override
	public int getDuration() {
		return duration;
	}


	@Override
	public RecipeChoice[] getIngredients() {
		return new RecipeChoice[] { this.ingredient };
	}


	@Override
	public ItemStack[] getExtraResults() {
		return new ItemStack[0];
	}


	@Override
	public ItemStack createIcon(int cycle) {
		ItemStack ingredient = RecipeInventoryTask.getItemFromItemChoice(this.ingredient, ResultChoice.ORDERED, cycle);
		ItemMeta meta = ingredient.getItemMeta();
		return new ItemBuilder(ingredient).setName((meta.hasDisplayName() ? meta.getDisplayName() : "§f" + Utils.toCapitalWords(ingredient.getType())) + " §9- §3" + DURATION.format(this.duration / 20.0D)).build();
	}


	public int getMaxChance() {
		return this.maxChance;
	}


	public Map<ItemStack, Integer> getOutputs() {
		return this.outputs;
	}


	@SuppressWarnings("deprecation")
	@Override
	public JsonObject saveRecipe() {
		JsonObject object = new JsonObject();
		object.addProperty("name", this.getIdentifier());
		object.add("ingredient", JsonUtils.convertItemStack(this.ingredient.getItemStack()));
		return object;
	}


	/**
	 * Reads and returns a random output recipe from the given json object and given machine.
	 * 
	 * @param object  the json object
	 * @param machine the machine
	 * @return the read recipe
	 */
	public static RandomOutputRecipe loadRecipe(JsonObject object, AbstractMachine machine) {
		String identifier = object.get("name").getAsString();
		ItemStack ingredient = JsonUtils.parseItemStack(object.get("ingredient").getAsJsonObject());
		int duration = object.get("duration").getAsInt();
		RandomOutputRecipe recipe = new RandomOutputRecipe(machine, identifier, ingredient, duration);
		for(JsonElement element : object.get("output").getAsJsonArray()) {
			JsonObject elementobject = element.getAsJsonObject();
			int chance = elementobject.get("chance").getAsInt();
			ItemStack stack = JsonUtils.parseItemStack(elementobject.get("item").getAsJsonObject());
			recipe.addOutput(stack, chance);
		}
		return recipe;
	}

}
