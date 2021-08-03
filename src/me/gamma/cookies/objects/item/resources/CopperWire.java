
package me.gamma.cookies.objects.item.resources;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class CopperWire extends AbstractCustomItem {

	@Override
	public String getRegistryName() {
		return "copper_wire";
	}


	@Override
	public String getDisplayName() {
		return "§6Copper Wire";
	}


	@Override
	public Material getMaterial() {
		return Material.STRING;
	}


	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.COPPER_WIRE;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), 8, RecipeCategory.RESOURCES, RecipeType.ENGINEER);
		recipe.setShape("CCC");
		recipe.setIngredient('C', Material.COPPER_INGOT);
		return recipe;
	}
	
	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		event.setCancelled(true);
	}

}
