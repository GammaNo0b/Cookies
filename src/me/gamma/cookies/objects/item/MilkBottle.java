package me.gamma.cookies.objects.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.util.ItemBuilder;

public class MilkBottle extends Drink {

	public MilkBottle() {
		super("§fMilk Bottle", "milk_bottle", 4, 6, null, Color.WHITE);
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(Cookies.INSTANCE, "milk_bottle"), new ItemBuilder(this.createDefaultItemStack()).setAmount(4).build()).shape("M").setIngredient('M', Material.MILK_BUCKET);
		RecipeCategory.KITCHEN_INGREDIENTS.registerRecipe(recipe);
		return recipe;
	}

}
