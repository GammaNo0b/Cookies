
package me.gamma.cookies.listeners;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.google.common.collect.Lists;

import me.gamma.cookies.managers.RecipeManager;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.recipe.CookieRecipe;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;



public class CustomCraftingListener implements Listener {

	public static final String CUSTOM_CRAFTING_TITLE = "§2Custom §aCrafting";
	
	@EventHandler
	public void onCraft(CraftItemEvent event) {
		if(!(event.getRecipe() instanceof CookieRecipe)) {
			for(ItemStack ingredient : event.getInventory().getMatrix()) {
				if(AbstractCustomItem.isCustomItem(ingredient)) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getWhoClicked() instanceof Player) {
			RecipeType type = RecipeType.byName(event.getView().getTitle());
			if(type != null) {
				if(!(event.getClickedInventory() instanceof PlayerInventory)) {
					event.setCancelled(!Lists.newArrayList(1, 2, 3, 10, 11, 12, 15, 19, 20, 21).contains(event.getSlot()));
					if(event.getSlot() == 13 && event.getInventory().getItem(15) == null) {
						ItemStack[][] ingredients = new ItemStack[3][3];
						int min = 64;
						for(int i = 0; i < 3; i++) {
							for(int j = 0; j < 3; j++) {
								ItemStack current = event.getClickedInventory().getItem(i * 9 + j + 1);
								if(current != null) {
									min = Math.min(min, current.getAmount());
								}
								ingredients[i][j] = current;
							}
						}
						for(CookieRecipe recipe : RecipeManager.getRegisteredCustomRecipes(type)) {
							if(recipe instanceof CustomRecipe) {
								if(((CustomRecipe) recipe).matches(ingredients)) {
									min = Math.min(min, recipe.getResult().getType().getMaxStackSize() / recipe.getResult().getAmount());
									for(int i = 0; i < 3; i++) {
										for(int j = 0; j < 3; j++) {
											ItemStack current = event.getClickedInventory().getItem(i * 9 + j + 1);
											if(current != null) {
												current.setAmount(current.getAmount() - min);
											}
											event.getClickedInventory().setItem(i * 9 + j + 1, current);
										}
									}
									event.getClickedInventory().setItem(15, new ItemBuilder(recipe.getResult()).setAmount(min * recipe.getResult().getAmount()).build());
								}
							}
						}
					}
				}
			}
		}
	}


	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if(RecipeType.byName(event.getView().getTitle()) != null) {
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					Utilities.giveItemToPlayer(event.getPlayer(), event.getInventory().getItem((i + 1) * 9 + j + 1));
				}
			}
		}
	}


	public static void openCustomCraftingGui(Player player, RecipeType type) {
		if(type == RecipeType.MACHINE)
			return;
		Inventory craftingGui = Bukkit.createInventory(null, 5 * 9, type.getName());
		for(int i = 0; i < 9; i++) {
			craftingGui.setItem(i, new ItemBuilder(type.getBorder()).setName(" ").build());
			craftingGui.setItem(craftingGui.getSize() - 1 - i, new ItemBuilder(type.getBorder()).setName(" ").build());
		}
		final int[] border = new int[] {
			9, 17, 18, 26, 27, 35
		};
		for(int i : border) {
			craftingGui.setItem(i, new ItemBuilder(type.getBorder()).setName(" ").build());
		}
		final int[] background = new int[] {
			13, 14, 15, 16, 23, 25, 31, 32, 33, 34
		};
		for(int i : background) {
			craftingGui.setItem(i, new ItemBuilder(type.getBackground()).setName(" ").build());
		}
		craftingGui.setItem(22, new ItemBuilder(type.getIcon()).setName("§9Craft").build());
		player.openInventory(craftingGui);
	}

}
