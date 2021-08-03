
package me.gamma.cookies.objects.block.skull;


import me.gamma.cookies.objects.recipe.RecipeCategory;



public class Pebble extends CustomSkullBlock {

	public Pebble(String identifier, String name, String texture) {
		super(identifier, name, () -> RecipeCategory.RESOURCES, texture);
	}

}
