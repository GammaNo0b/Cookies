
package me.gamma.cookies.objects.item;


import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.ShowcaseRecipe;



public class CustomItem extends AbstractCustomItem {

	private final String identifier;
	private final String name;
	private final Material material;
	private Supplier<RecipeCategory> category;
	private final int customModelData;

	public CustomItem(String identifier, String name, Material material, Supplier<RecipeCategory> category, int customModelData) {
		this.identifier = identifier;
		this.name = name;
		this.material = material;
		this.category = category;
		this.customModelData = customModelData;
	}


	@Override
	public String getRegistryName() {
		return this.identifier;
	}


	@Override
	public String getDisplayName() {
		return this.name;
	}


	@Override
	public Material getMaterial() {
		return this.material;
	}


	@Override
	public int getCustomModelData() {
		return this.customModelData;
	}


	@Override
	public Recipe getRecipe() {
		return new ShowcaseRecipe(this.createDefaultItemStack(), category.get());
	}

}
