
package me.gamma.cookies.objects.recipe;


import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.ItemBuilder;



public class SimpleMachineRecipe implements MachineRecipe {

	private String identifier;
	private ItemStack result;
	private ItemStack ingredient;
	private int duration;

	public SimpleMachineRecipe(String identifier, ItemStack result, ItemStack ingredient, int duration) {
		this.identifier = identifier;
		this.result = result;
		this.ingredient = ingredient;
		this.duration = duration;
	}

	public SimpleMachineRecipe(String identifier, ItemStack result, int amount, ItemStack ingredient, int duration) {
		this(identifier, new ItemBuilder(result).setAmount(amount).build(), ingredient, duration);
	}

	public SimpleMachineRecipe(String identifier, ItemStack result, RecipeCategory category, ItemStack ingredient, int duration) {
		this(identifier, result, ingredient, duration);
		category.registerRecipe(this);
	}

	public SimpleMachineRecipe(String identifier, ItemStack result, int amount, RecipeCategory category, ItemStack ingredient, int duration) {
		this(identifier, new ItemBuilder(result).setAmount(amount).build(), category, ingredient, duration);
	}
	
	
	@Override
	public String getIdentifier() {
		return this.identifier;
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
		return duration;
	}


	@Override
	public ItemStack[] getIngredients() {
		return new ItemStack[] {
			this.ingredient
		};
	}

}
