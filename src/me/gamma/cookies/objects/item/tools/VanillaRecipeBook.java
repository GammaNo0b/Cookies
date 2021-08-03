
package me.gamma.cookies.objects.item.tools;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.managers.InventoryManager;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class VanillaRecipeBook extends AbstractCustomItem {

	@Override
	public String getRegistryName() {
		return "vanilla_recipe_book";
	}


	@Override
	public String getDisplayName() {
		return "§aVanilla Recipe Book";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Search for every Recipe in Vanilla!");
	}


	@Override
	public Material getMaterial() {
		return Material.KNOWLEDGE_BOOK;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MISCELLANEOUS, RecipeType.CUSTOM);
		recipe.setShape(" A ", "CBD", " E ");
		recipe.setIngredient('A', Material.STICK);
		recipe.setIngredient('B', Material.BOOK);
		recipe.setIngredient('C', Material.INK_SAC);
		recipe.setIngredient('D', Material.IRON_NUGGET);
		recipe.setIngredient('E', Material.EMERALD);
		return recipe;
	}


	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		event.setCancelled(true);
		InventoryManager.openVanillaRecipeList(player);
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		event.setCancelled(true);
		InventoryManager.openVanillaRecipeList(player);
	}

}
