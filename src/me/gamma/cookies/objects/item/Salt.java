package me.gamma.cookies.objects.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.RecipeCategory;

public class Salt extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "salt";
	}

	@Override
	public String getDisplayName() {
		return "§7Salt";
	}

	@Override
	public Material getMaterial() {
		return Material.SUGAR;
	}
	
	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.SALT;
	}

	@Override
	public Recipe getRecipe() {
		Recipe recipe = new ShapelessRecipe(new NamespacedKey(Cookies.INSTANCE, "salt_from_water"), this.createDefaultItemStack()).addIngredient(Material.WATER_BUCKET);
		RecipeCategory.KITCHEN_INGREDIENTS.registerRecipe(recipe);
		return recipe;
	}

}
