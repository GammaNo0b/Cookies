
package me.gamma.cookies.objects.item.resources;


import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.ShowcaseRecipe;



public class Dust extends AbstractCustomItem {

	private String identifier;
	private String name;
	private Material material;
	private int customModelData;

	public Dust(String identifier, String name, Material material) {
		this(identifier, name, material, -1);
	}


	public Dust(String identifier, String name, Material material, int customModelData) {
		this.identifier = identifier;
		this.name = name;
		this.material = material;
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
		return new ShowcaseRecipe(this.createDefaultItemStack(), RecipeCategory.RESOURCES);
	}

}
