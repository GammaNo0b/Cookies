package me.gamma.cookies.objects.item;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.ShowcaseRecipe;


public class Toast extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "toast";
	}


	@Override
	public String getDisplayName() {
		return "§6Toast";
	}


	@Override
	public Material getMaterial() {
		return Material.BREAD;
	}
	
	
	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.TOAST;
	}


	@Override
	public Recipe getRecipe() {
		return new ShowcaseRecipe(this.createDefaultItemStack(), RecipeCategory.KITCHEN_INGREDIENTS);
	}

}
