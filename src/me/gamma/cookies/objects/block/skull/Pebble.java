
package me.gamma.cookies.objects.block.skull;


import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.ShowcaseRecipe;



public class Pebble extends AbstractSkullBlock {

	private String identifier;
	private String name;
	private String texture;

	public Pebble(String identifier, String name, String texture) {
		this.identifier = identifier;
		this.name = name;
		this.texture = texture;

	}


	@Override
	public String getBlockTexture() {
		return texture;
	}


	@Override
	public String getRegistryName() {
		return identifier;
	}


	@Override
	public String getDisplayName() {
		return name;
	}


	@Override
	public Recipe getRecipe() {
		return new ShowcaseRecipe(this.createDefaultItemStack(), RecipeCategory.RESOURCES);
	}

}
