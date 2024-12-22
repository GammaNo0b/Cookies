
package me.gamma.cookies.object.recipe.machine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import com.google.gson.JsonObject;

import me.gamma.cookies.object.block.machine.AbstractMachine;
import me.gamma.cookies.object.recipe.CookieRecipe;
import me.gamma.cookies.object.recipe.RecipeType;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.RecipeUtils;



public interface MachineRecipe extends CookieRecipe {

	/**
	 * The title of the recipe inventory gui.
	 */
	String MACHINE_RECIPE_TITLE = "§bMachine Recipe";

	/**
	 * Returns a unique identifier string for this recipe.
	 * 
	 * @return the identifier string
	 */
	String getIdentifier();

	/**
	 * Returns the machine that has this recipe registered.
	 * 
	 * @return the machine
	 */
	AbstractMachine getMachine();

	/**
	 * Returns the number of ticks this recipe needs to finish.
	 * 
	 * @return the duration
	 */
	int getDuration();

	/**
	 * Returns an array of ingredients needed to start this recipe.
	 * 
	 * @return the ingredients
	 */
	RecipeChoice[] getIngredients();

	/**
	 * Returns the array of extra results this recipe can output.
	 * 
	 * @return the extra results
	 */
	ItemStack[] getExtraResults();

	/**
	 * Returns an icon that gets displayed for this recipe.
	 * 
	 * @param cycle the cycle
	 * @return the icon
	 */
	ItemStack createIcon(int cycle);


	@Override
	default RecipeType getType() {
		return RecipeType.MACHINE;
	}


	default HashMap<RecipeChoice, Integer> matches(ItemStack[] input) {
		if(this.getIngredients() == null)
			return null;

		HashMap<ItemStack, Integer> map = new HashMap<>();
		for(ItemStack stack : input) {
			if(!ItemUtils.isEmpty(stack)) {
				stack = stack.clone();
				int amount = stack.getAmount();
				stack.setAmount(1);
				if(!map.containsKey(stack))
					map.put(stack, amount);
				else
					map.put(stack, amount + map.get(stack));
			}
		}

		return this.checkIngredients(ArrayUtils.asList(this.getIngredients()), 0, map);
	}


	private HashMap<RecipeChoice, Integer> checkIngredients(ArrayList<RecipeChoice> ingredients, int index, HashMap<ItemStack, Integer> map) {
		if(index >= ingredients.size())
			return new HashMap<>();

		RecipeChoice ingredient = ingredients.get(index);
		List<ItemStack> choices = RecipeUtils.getItemsFromChoice(ingredient);

		for(int i = 0; i < choices.size(); i++) {
			ItemStack stack = choices.get(i);
			int amount = stack.getAmount();
			stack.setAmount(1);

			if(!map.containsKey(stack))
				continue;

			int available = map.get(stack);
			available -= amount;
			if(available < 0)
				continue;

			map.put(stack, available);
			HashMap<RecipeChoice, Integer> uses = this.checkIngredients(ingredients, index + 1, new HashMap<>(map));
			if(uses != null) {
				uses.put(ingredient, i);
				return uses;
			}

			available += amount;
			map.put(stack, available);
		}

		return null;
	}


	default List<ItemStack> getOutputs(double fortune) {
		// store the results from the current crafting recipe
		List<ItemStack> tempResults = new ArrayList<>();
		ItemStack result = this.getResult();
		if(!ItemUtils.isEmpty(result))
			tempResults.add(result);
		for(ItemStack extra : this.getExtraResults())
			if(!ItemUtils.isEmpty(extra))
				tempResults.add(extra);

		// increase amount of items
		List<ItemStack> results = new ArrayList<>();
		int multiplier = (int) Math.floor(fortune);
		double chance = fortune - multiplier;
		for(ItemStack stack : tempResults) {
			int amount = stack.getAmount() * multiplier;
			if(Math.random() < chance)
				amount += stack.getAmount();

			int max = stack.getMaxStackSize();
			int full = amount / max;
			for(int i = 0; i < full; i++) {
				ItemStack copy = stack.clone();
				copy.setAmount(max);
				results.add(copy);
			}
			ItemStack copy = stack.clone();
			copy.setAmount(amount - full);
			results.add(copy);
		}
		
		return results;
	}

	JsonObject saveRecipe();

	/**
	 * Reads and returns a machine recipe from the given json object and given machine.
	 * 
	 * @param object  the json object
	 * @param machine the machine
	 * @return the read recipe
	 */
	public static MachineRecipe loadRecipe(JsonObject object, AbstractMachine machine) {
		String machinetype = object.get("type").getAsString();
		if("advanced".equals(machinetype)) {
			return AdvancedMachineRecipe.loadRecipe(object, machine);
		} else if("random".equals(machinetype)) {
			return RandomOutputRecipe.loadRecipe(object, machine);
		} else if("randoms".equals(machinetype)) {
			return RandomOutputsRecipe.loadRecipe(object, machine);
		} else {
			return SimpleMachineRecipe.loadRecipe(object, machine);
		}
	}

}
