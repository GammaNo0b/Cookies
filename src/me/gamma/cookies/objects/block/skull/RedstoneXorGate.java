package me.gamma.cookies.objects.block.skull;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class RedstoneXorGate extends Abstract2I1ORedstoneGate {

	@Override
	protected boolean calculateOutput(boolean left, boolean right) {
		return left ^ right;
	}


	@Override
	public String getRegistryName() {
		return "redstone_xor_gate";
	}


	@Override
	public String getDisplayName() {
		return "§6XOR §cGate";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.REDSTONE, RecipeType.ENGINEER);
		recipe.setShape("T", "RTR", "SSS");
		recipe.setIngredient('S', Material.SMOOTH_STONE_SLAB);
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('T', Material.REDSTONE_TORCH);
		return recipe;
	}

}
