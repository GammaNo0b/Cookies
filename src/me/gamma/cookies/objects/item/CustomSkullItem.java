
package me.gamma.cookies.objects.item;


import java.util.function.Supplier;

import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.ShowcaseRecipe;



public class CustomSkullItem extends AbstractSkullItem {

	private String texture;
	private String identifier;
	private String name;
	private Supplier<RecipeCategory> category;

	public CustomSkullItem(String identifier, String name, Supplier<RecipeCategory> category, String texture) {
		this.identifier = identifier;
		this.name = name;
		this.category = category;
		this.texture = texture;
	}


	@Override
	protected String getBlockTexture() {
		return this.texture;
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
	public Recipe getRecipe() {
		return new ShowcaseRecipe(this.createDefaultItemStack(), this.category.get());
	}

}
