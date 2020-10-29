package me.gamma.cookies.objects.recipe;

import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.ItemBuilder;

public class CompressionRecipe extends SimpleMachineRecipe {

	public CompressionRecipe(String identifier, ItemStack result, ItemStack ingredient, int duration) {
		super(identifier, result, ingredient, duration);
	}

	public CompressionRecipe(String identifier, ItemStack result, ItemStack ingredient, int amount, int duration) {
		super(identifier, result, new ItemBuilder(ingredient).setAmount(amount).build(), duration);
	}

	public CompressionRecipe(String identifier, ItemStack result, RecipeCategory category, ItemStack ingredient, int duration) {
		super(identifier, result, category, ingredient, duration);
	}

	public CompressionRecipe(String identifier, ItemStack result, RecipeCategory category, ItemStack ingredient, int amount, int duration) {
		super(identifier, result, category, new ItemBuilder(ingredient).setAmount(amount).build(), duration);
	}

}
