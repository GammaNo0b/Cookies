package me.gamma.cookies.objects.item.tools;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;


public class PortableEngineeringStation extends PortableCustomCraftingOpener {

	@Override
	protected RecipeType getType() {
		return RecipeType.ENGINEER;
	}


	@Override
	protected String getBlockTexture() {
		return HeadTextures.SMITHING_TABLE;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MISCELLANEOUS, RecipeType.CUSTOM);
		recipe.setShape("F  ", "W T", "BCS");
		recipe.setIngredient('F', Material.NETHER_BRICK_FENCE);
		recipe.setIngredient('W', Material.STONE_BRICK_WALL);
		recipe.setIngredient('B', Material.BLAST_FURNACE);
		recipe.setIngredient('C', Material.CARTOGRAPHY_TABLE);
		recipe.setIngredient('S', Material.SMITHING_TABLE);
		recipe.setIngredient('T', CustomItemSetup.PORTABLE_CUSTOM_CRAFTING_TABLE.createDefaultItemStack());
		return recipe;
	}

}
