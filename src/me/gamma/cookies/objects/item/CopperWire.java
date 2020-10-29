package me.gamma.cookies.objects.item;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;


public class CopperWire extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "copper_wire";
	}


	@Override
	public String getDisplayName() {
		return "§6Copper Wire";
	}


	@Override
	public Material getMaterial() {
		return Material.STRING;
	}
	
	
	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.COPPER_WIRE;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), 8, RecipeCategory.RESOURCES, RecipeType.ENGINEER);
		recipe.setShape("CCC");
		recipe.setIngredient('C', CustomItemSetup.COPPER_INGOT.createDefaultItemStack());
		return recipe;
	}

}
