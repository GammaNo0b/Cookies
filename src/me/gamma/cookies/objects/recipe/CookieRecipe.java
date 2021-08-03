
package me.gamma.cookies.objects.recipe;


import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.objects.item.AbstractCustomItem;



public interface CookieRecipe extends Recipe {

	RecipeType getType();


	public static boolean sameType(ItemStack item1, ItemStack item2) {
		if(item1 == null) {
			return item2 == null;
		}
		if(item2 == null) {
			return false;
		}

		if(item1.getType() != item2.getType()) {
			return false;
		}

		final boolean custom1 = AbstractCustomItem.isCustomItem(item1);
		final boolean custom2 = AbstractCustomItem.isCustomItem(item2);

		if(custom1 ^ custom2) {
			return false;
		}

		return AbstractCustomItem.IDENTIFIER.isSame(item1.getItemMeta(), item2.getItemMeta());
	}


	public static boolean sameIngredients(ItemStack... items) {
		if(items.length < 2) {
			return true;
		}
		ItemStack stack = items[0];
		for(int i = 1; i < items.length; i++) {
			ItemStack other = items[i];
			if(sameIngredient(stack, other)) {
				stack = other;
			} else {
				return false;
			}
		}
		return true;
	}


	public static boolean sameIngredient(ItemStack item1, ItemStack item2) {
		if(item1 == null)
			return item2 == null;

		if(item2 == null)
			return false;

		if(item1.getType() != item2.getType())
			return false;

		ItemMeta meta1 = item1.getItemMeta();
		ItemMeta meta2 = item2.getItemMeta();

		if(meta1 == null)
			return meta2 == null;

		if(meta2 == null)
			return false;

		if(!meta1.getDisplayName().equals(meta2.getDisplayName()))
			return false;

		if(meta1.hasCustomModelData() ^ meta2.hasCustomModelData())
			return false;

		if(meta1.hasCustomModelData())
			if(meta1.getCustomModelData() != meta2.getCustomModelData())
				return false;

		if(!meta1.getEnchants().equals(meta2.getEnchants()))
			return false;

		if(!meta1.getItemFlags().equals(meta2.getItemFlags()))
			return false;

		if(!meta1.getLocalizedName().equals(meta2.getLocalizedName()))
			return false;

		return Bukkit.getItemFactory().equals(meta1, meta2);
	}

}
