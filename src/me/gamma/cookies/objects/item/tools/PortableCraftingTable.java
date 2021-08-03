package me.gamma.cookies.objects.item.tools;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class PortableCraftingTable extends PortableInventoryOpener {

	@Override
	protected void openInventory(Player player) {
		player.openWorkbench(null, true);
	}

	@Override
	protected String getBlockTexture() {
		return HeadTextures.CRAFTING_TABLE;
	}

	@Override
	public String getRegistryName() {
		return "portable_crafting_table";
	}

	@Override
	public String getDisplayName() {
		return "§6Portable Crafting Table";
	}

	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MISCELLANEOUS, RecipeType.CUSTOM);
		recipe.setShape("CSW");
		recipe.setIngredient('C', Material.CRAFTING_TABLE);
		recipe.setIngredient('S', Material.STICK);
		recipe.setIngredient('W', Material.STRING);
		return recipe;
	}

}
