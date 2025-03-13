
package me.gamma.cookies.object.recipe.machine;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.object.block.machine.AbstractMachine;
import me.gamma.cookies.object.gui.task.RecipeInventoryTask.ResultChoice;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.JsonUtils;



public class RandomOutputsRecipe implements MachineRecipe {

	private static final DecimalFormat DURATION = new DecimalFormat("0.0s");

	private final AbstractMachine machine;
	private final Random r;
	private final ItemStack ingredient;
	private final RecipeChoice[] ingredients;
	private final List<Result> outputs = new ArrayList<>();
	private final String identifier;
	private final int duration;

	public RandomOutputsRecipe(AbstractMachine machine, String identifier, ItemStack ingredient, int duration) {
		this(machine, identifier, ingredient, duration, new Random());
	}


	public RandomOutputsRecipe(AbstractMachine machine, String identifier, ItemStack ingredient, int duration, long seed) {
		this(machine, identifier, ingredient, duration, new Random(seed));
	}


	public RandomOutputsRecipe(AbstractMachine machine, String identifier, ItemStack ingredient, int duration, Random random) {
		this.machine = machine;
		this.identifier = identifier;
		this.ingredient = ingredient;
		this.ingredients = ingredient == null ? new RecipeChoice[0] : new RecipeChoice[] { new RecipeChoice.ExactChoice(ingredient) };
		this.duration = duration;
		this.r = random;
	}


	@Override
	public ItemStack getResult() {
		return null;
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
		List<ItemStack> results = new ArrayList<>();
		for(Result result : this.outputs) {
			ItemStack stack = result.getResult(this.r);
			if(ItemUtils.isEmpty(stack))
				continue;

			ItemUtils.getItems(stack, stack.getAmount(), results);
		}
		return results.toArray(ItemStack[]::new);
	}


	@Override
	public ItemStack createIcon(int cycle) {
		if(this.ingredient != null) {
			ItemStack stack = this.ingredient.clone();
			ItemMeta meta = stack.getItemMeta();
			List<String> lore = meta.getLore();
			if(lore == null)
				lore = new ArrayList<>();

			lore.add(0, String.format("  §6Duration: §e%ss", DURATION.format(this.duration / 20.0D)));
			meta.setLore(lore);
			stack.setItemMeta(meta);
			return stack;
		}

		return ResultChoice.ORDERED.chooseItem(this.outputs, this.outputs.size(), cycle).createIcon(this.duration);
	}


	public List<Result> getResults() {
		return this.outputs;
	}


	public RandomOutputsRecipe addOutput(Material material, double chance, int min, int max) {
		return this.addOutput(new ItemStack(material), chance, min, max);
	}


	public RandomOutputsRecipe addOutput(IItemSupplier item, double chance, int min, int max) {
		return this.addOutput(item.get(), chance, min, max);
	}


	public RandomOutputsRecipe addOutput(ItemStack result, double chance, int min, int max) {
		this.outputs.add(new Result(result, chance, min, max));
		return this;
	}

	public record Result(ItemStack result, double chance, int min, int max) {

		private ItemStack getResult(Random random) {
			if(random.nextDouble() > this.chance)
				return null;

			int amount = random.nextInt(this.min, this.max + 1);
			if(amount <= 0)
				return null;

			ItemStack stack = this.result.clone();
			stack.setAmount(amount);
			return stack;
		}


		public ItemStack createIcon(int duration) {
			ItemStack stack = this.result.clone();
			ItemMeta meta = stack.getItemMeta();
			List<String> lore = meta.getLore();
			if(lore == null)
				lore = new ArrayList<>();

			if(duration > 0)
				lore.add(String.format("  §6Duration: §e%.2s", DURATION.format(duration / 20.0D)));
			if(this.chance < 1.0D)
				lore.add(String.format("  §3Chance: §b%.2f%%", this.chance * 100.0D));
			if(this.min == this.max)
				stack.setAmount(this.min);
			else
				lore.add(String.format("  §2Amount: §a%d §8- §a%d", this.min, this.max));

			meta.setLore(lore);
			stack.setItemMeta(meta);
			return stack;
		}

	}

	@Override
	public JsonObject saveRecipe() {
		JsonObject object = new JsonObject();
		object.addProperty("name", this.getIdentifier());
		if(this.ingredient != null)
			object.add("ingredient", JsonUtils.convertItemStack(this.ingredient));
		JsonArray array = new JsonArray(this.outputs.size());
		object.add("output", array);
		for(Result result : this.outputs) {
			JsonObject elementObject = new JsonObject();
			elementObject.add("result", JsonUtils.convertItemStack(result.result));
			elementObject.addProperty("chance", result.chance);
			elementObject.addProperty("min", result.min);
			elementObject.addProperty("max", result.max);
			array.add(elementObject);
		}
		return object;
	}


	/**
	 * Reads and returns a random outputs recipe from the given json object and given machine.
	 * 
	 * @param object  the json object
	 * @param machine the machine
	 * @return the read recipe
	 */
	public static RandomOutputsRecipe loadRecipe(JsonObject object, AbstractMachine machine) {
		String identifier = object.get("name").getAsString();
		int duration = object.get("duration").getAsInt();
		ItemStack ingredient = null;
		if(object.has("ingredient"))
			ingredient = JsonUtils.parseItemStack(object.get("ingredient").getAsJsonObject());
		RandomOutputsRecipe recipe = new RandomOutputsRecipe(machine, identifier, ingredient, duration);
		for(JsonElement element : object.get("output").getAsJsonArray()) {
			JsonObject elementObject = element.getAsJsonObject();
			ItemStack result = JsonUtils.parseItemStack(elementObject.get("result").getAsJsonObject());
			double chance = elementObject.get("chance").getAsDouble();
			int min = elementObject.get("min").getAsInt();
			int max = elementObject.get("max").getAsInt();
			recipe.addOutput(result, chance, min, max);
		}
		return recipe;
	}

}
