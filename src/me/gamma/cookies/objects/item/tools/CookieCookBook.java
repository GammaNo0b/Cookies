
package me.gamma.cookies.objects.item.tools;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.managers.InventoryManager;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.property.BooleanProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class CookieCookBook extends AbstractCustomItem {

	private static BooleanProperty OPEN_ON_EAT = new BooleanProperty("eat");

	@Override
	public String getRegistryName() {
		return "cookie_cook_book";
	}


	@Override
	public String getDisplayName() {
		return "§6Cookie Cook Book";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Collection of all Recipes", "§7written down in a Cook Book.");
	}


	@Override
	public Material getMaterial() {
		return Material.COOKIE;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		ItemMeta meta = stack.getItemMeta();
		OPEN_ON_EAT.store(meta, true);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MISCELLANEOUS, RecipeType.CUSTOM);
		recipe.setShape(" H ", "SGB", " C ");
		recipe.setIngredient('H', Material.HONEY_BOTTLE);
		recipe.setIngredient('S', Material.SUGAR);
		recipe.setIngredient('G', Material.BOOK);
		recipe.setIngredient('B', Material.COCOA_BEANS);
		recipe.setIngredient('C', Material.COOKIE);
		return recipe;
	}


	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		if(this.openCookieCookBook(player, stack))
			event.setCancelled(true);
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		if(this.openCookieCookBook(player, stack))
			event.setCancelled(true);
	}


	private boolean openCookieCookBook(Player player, ItemStack stack) {
		if(player.isSneaking()) {
			ItemMeta meta = stack.getItemMeta();
			if(OPEN_ON_EAT.toggle(meta)) {
				player.sendMessage("§aOpen on Eat enabled!");
			} else {
				player.sendMessage("§cOpen on Eat disabled!");
			}
			stack.setItemMeta(meta);
			return true;
		} else if(!OPEN_ON_EAT.fetch(stack.getItemMeta())) {
			InventoryManager.openRecipeCategoryList(player, 0, false);
			return true;
		}
		return false;
	}


	@Override
	public void onPlayerConsumesItem(Player player, ItemStack stack, PlayerItemConsumeEvent event) {
		InventoryManager.openRecipeCategoryList(player, 0, false);
		event.setCancelled(true);
	}

}
