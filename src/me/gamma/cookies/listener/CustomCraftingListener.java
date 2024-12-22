
package me.gamma.cookies.listener;


import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.init.RecipeInit;
import me.gamma.cookies.object.recipe.CookieRecipe;
import me.gamma.cookies.object.recipe.CustomRecipe;
import me.gamma.cookies.object.recipe.RecipeType;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.Pair;



public class CustomCraftingListener implements Listener {

	public static final String CUSTOM_CRAFTING_TITLE = "§2Custom §aCrafting";

	/**
	 * Get's executed when a player crafts an item in the vanilla crafting table.
	 * 
	 * @param event the {@link CraftItemEvent}
	 */
	@EventHandler
	public void onCraft(CraftItemEvent event) {
		// cancel craftng if any custom item is involved
		if(!(event.getRecipe() instanceof CookieRecipe))
			for(ItemStack ingredient : event.getInventory().getMatrix())
				if(ItemUtils.isCustomItem(ingredient))
					event.setCancelled(true);
	}


	/**
	 * Get's executed when a player clicks in an inventory.
	 * 
	 * @param event the {@link InventoryClickEvent}
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		// determine recipe type
		RecipeType type = RecipeType.byName(event.getView().getTitle());
		if(type == null)
			return;

		if(event.getClickedInventory() instanceof PlayerInventory)
			return;

		if(InventoryUtils.isFiller(event.getCurrentItem())) {
			event.setCancelled(true);
			return;
		}

		if(event.getSlot() == type.getCraftingSlot()) {
			event.setCancelled(true);

			Inventory inventory = event.getClickedInventory();
			Pair<ItemStack[][], Integer> pair = this.listIngredients(inventory, type);
			ItemStack[][] ingredients = pair.left;

			CustomRecipe recipe = this.findRecipe(ingredients, type);
			if(recipe == null)
				return;

			int amount = Math.min(pair.right, recipe.getResult().getMaxStackSize() / recipe.getResult().getAmount());
			ItemStack rest = this.setResult(inventory, recipe, ingredients, amount);
			ItemUtils.giveItemToPlayer(event.getWhoClicked(), rest);
			this.removeIngredients(event.getWhoClicked(), inventory, type, amount);
		}
	}


	/**
	 * Lists the ingredients in the given inventory
	 * 
	 * @param inventory the inventory
	 * @param type      the recipe type
	 * @return a pair containing the ingredient matrix and the minimum stacksize of each ingredient
	 */
	private Pair<ItemStack[][], Integer> listIngredients(Inventory inventory, RecipeType type) {
		ItemStack[][] ingredients = new ItemStack[type.getHeight()][type.getWidth()];
		int min = Integer.MAX_VALUE;
		for(int i = 0; i < type.getHeight(); i++) {
			for(int j = 0; j < type.getWidth(); j++) {
				ItemStack current = inventory.getItem((i + type.getStartY()) * 9 + j + type.getStartX());
				if(!ItemUtils.isEmpty(current))
					min = Math.min(min, current.getAmount());
				ingredients[i][j] = current;
			}
		}

		return new Pair<>(ingredients, min);
	}


	/**
	 * Removes amount ingredients of all stacks in the given inventory.
	 * 
	 * @param player    the player that executed the crafting operation
	 * @param inventory the inventory
	 * @param type      the recipe type
	 * @param amount    the amount by which each ingredient should be shrinked
	 */
	private void removeIngredients(HumanEntity player, Inventory inventory, RecipeType type, int amount) {
		for(int i = 0; i < type.getHeight(); i++) {
			for(int j = 0; j < type.getWidth(); j++) {
				ItemStack current = inventory.getItem((i + type.getStartY()) * 9 + j + type.getStartX());
				if(current != null) {
					Material remaining = ItemUtils.getCraftingRemainingItem(current.getType());
					current.setAmount(current.getAmount() - amount);
					if(remaining != null)
						ItemUtils.giveItemToPlayer(player, new ItemStack(remaining, amount));
				}
				inventory.setItem((i + type.getStartY()) * 9 + j + type.getStartX(), current);
			}
		}
	}


	/**
	 * Determines the recipe using the given ingredient matrix.
	 * 
	 * @param ingredients the ingredient matrix
	 * @param type        the recipe type
	 * @return the determined recipe or null
	 */
	private CustomRecipe findRecipe(ItemStack[][] ingredients, RecipeType type) {
		return RecipeInit.getRegisteredCustomRecipes(type).stream().map(r -> r instanceof CustomRecipe custom ? custom : null).filter(Objects::nonNull).filter(c -> c.matches(ingredients)).findFirst().orElse(null);
	}


	/**
	 * Puts the result of the given recipe of given amount in the given inventory.
	 * 
	 * @param inventory   the inventory
	 * @param recipe      the recipe
	 * @param ingredients the ingredients
	 * @param amount      the amount
	 * @return the result that could not be stored
	 */
	private ItemStack setResult(Inventory inventory, CustomRecipe recipe, ItemStack[][] ingredients, int amount) {
		ItemStack result = recipe.getResult(ingredients);
		result.setAmount(result.getAmount() * amount);
		int slot = recipe.getType().getResultSlot();
		inventory.setItem(slot, ItemUtils.insertItemStack(result, inventory.getItem(slot)));
		return result;
	}


	/**
	 * Get's executed when a player closes an inventory.
	 * 
	 * @param event the {@link InventoryCloseEvent}
	 */
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		RecipeType type = RecipeType.byName(event.getView().getTitle());
		if(type == null)
			return;

		for(int i = 0; i < type.getHeight(); i++)
			for(int j = 0; j < type.getWidth(); j++)
				ItemUtils.giveItemToPlayer(event.getPlayer(), event.getInventory().getItem((i + type.getStartY()) * 9 + j + type.getStartX()));

		ItemUtils.giveItemToPlayer(event.getPlayer(), event.getInventory().getItem(type.getResultSlot()));
	}

}
