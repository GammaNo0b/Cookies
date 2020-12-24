
package me.gamma.cookies.objects.block.skull.storage;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.skull.AbstractSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.ConfigValues;



public class StorageConnector extends AbstractSkullBlock implements StorageComponent {

	public static boolean isConnector(TileState block) {
		return "storage_connector".equals(IDENTIFIER.fetch(block));
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.STORAGE_CONNECTOR;

	}


	@Override
	public String getDisplayName() {
		return "§bConnector";

	}


	@Override
	public String getRegistryName() {
		return "storage_connector";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Connects the Components in a Storage System.", "§7The maximum amount of Storage Connectors is " + ConfigValues.MAX_STORAGE_CONNECTORS + "!");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), 4, RecipeCategory.STORAGE, RecipeType.ENGINEER);
		recipe.setShape("GIG", "IRI", "GIG");
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('R', Material.REDSTONE);
		return recipe;
	}

}
