
package me.gamma.cookies.objects.block.skull;


import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class RedstoneOrGate extends Abstract2I1ORedstoneGate {

	@Override
	public String getRegistryName() {
		return "redstone_or_gate";
	}


	@Override
	public String getDisplayName() {
		return "§6OR §cGate";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.REDSTONE, RecipeType.ENGINEER);
		recipe.setShape("R", "S");
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('S', Material.SMOOTH_STONE_SLAB);
		return recipe;
	}


	@Override
	protected boolean calculateOutput(boolean left, boolean right) {
		return left || right;
	}

}
