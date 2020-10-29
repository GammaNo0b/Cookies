
package me.gamma.cookies.objects.item;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class GildedPaper extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "gilded_paper";
	}


	@Override
	public String getDisplayName() {
		return "§6Gilded Paper";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Shiny Paper!");
	}


	@Override
	public Material getMaterial() {
		return Material.MAP;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), 8, RecipeCategory.RESOURCES, RecipeType.CUSTOM);
		recipe.setShape("PPP", "PGP", "PPP");
		recipe.setIngredient('P', Material.PAPER);
		recipe.setIngredient('G', Material.GOLD_INGOT);
		return recipe;
	}


	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		event.setCancelled(true);
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		event.setCancelled(true);
	}


	@Override
	public void onEntityRightClick(Player player, ItemStack stack, Entity entity, PlayerInteractEntityEvent event) {
		event.setCancelled(true);
	}

}
