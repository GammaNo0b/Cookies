package me.gamma.cookies.objects.block.skull.machine;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;

public class ElectricalCircuit extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.ELECTRICAL_CIRCUIT;
	}

	@Override
	public String getRegistryName() {
		return "electrical_circuit";
	}

	@Override
	public String getDisplayName() {
		return "§7Electrical Circuit";
	}

	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ELECTRIC_COMPONENTS, RecipeType.ENGINEER);
		recipe.setShape("TRW", "LBR", "WRI");
		recipe.setIngredient('T', Material.REDSTONE_TORCH);
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('W', CustomItemSetup.COPPER_WIRE.createDefaultItemStack());
		recipe.setIngredient('L', Material.LIGHT_BLUE_DYE);
		recipe.setIngredient('B', Material.SMOOTH_STONE_SLAB);
		recipe.setIngredient('I', Material.IRON_NUGGET);
		return recipe;
	}

}
