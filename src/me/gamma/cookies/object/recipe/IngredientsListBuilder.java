
package me.gamma.cookies.object.recipe;


import java.util.ArrayList;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.util.ArrayUtils;



public class IngredientsListBuilder {

	private final Consumer<ArrayList<RecipeChoice>> onBuild;
	private final ArrayList<RecipeChoice> ingredients = new ArrayList<>();

	public IngredientsListBuilder(Consumer<ArrayList<RecipeChoice>> onBuild) {
		this.onBuild = onBuild;
	}


	public void build() {
		this.onBuild.accept(this.ingredients);
	}


	public IngredientsListBuilder add(Material... materials) {
		this.ingredients.add(new RecipeChoice.MaterialChoice(materials));
		return this;
	}


	public IngredientsListBuilder add(ItemStack... stacks) {
		this.ingredients.add(new RecipeChoice.ExactChoice(stacks));
		return this;
	}


	public IngredientsListBuilder add(IItemSupplier... supplier) {
		this.ingredients.add(new RecipeChoice.ExactChoice(ArrayUtils.map(supplier, IItemSupplier::get, ItemStack[]::new)));
		return this;
	}


	public IngredientsListBuilder add(RecipeChoice choice) {
		this.ingredients.add(choice);
		return this;
	}

}
