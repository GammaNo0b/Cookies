
package me.gamma.cookies.objects.item;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class Dough extends AbstractSkullItem {

	@Override
	protected String getBlockTexture() {
		return HeadTextures.DOUGH;
	}


	@Override
	public String getIdentifier() {
		return "dough";
	}


	@Override
	public String getDisplayName() {
		return "§6Dough";
	}


	@Override
	public Material getMaterial() {
		return Material.PLAYER_HEAD;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.KITCHEN_INGREDIENTS, RecipeType.KITCHEN);
		recipe.setShape("WS");
		recipe.setIngredient('W', Material.WHEAT);
		recipe.setIngredient('S', Material.SUGAR);
		return recipe;
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		event.setCancelled(true);
	}

}
