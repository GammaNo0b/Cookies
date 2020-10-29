
package me.gamma.cookies.objects.recipe;


import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.Cookies;



public interface CookieRecipe extends Recipe {

	RecipeType getType();


	public static boolean sameIngredient(ItemStack item1, ItemStack item2) {
		if(item1 == null ^ item2 == null) {
			return false;
		}
		if(item1 == null && item2 == null) {
			return true;
		}

		if(item1.getType() != item2.getType()) {
			return false;
		}

		ItemMeta meta1 = item1.getItemMeta();
		ItemMeta meta2 = item2.getItemMeta();

		if(!meta1.getDisplayName().equals(meta2.getDisplayName())) {
			return false;
		}
		if(meta1.hasCustomModelData() != meta2.hasCustomModelData()) {
			return false;
		}
		if(meta1.hasCustomModelData()) {
			if(meta1.getCustomModelData() != meta2.getCustomModelData()) {
				return false;
			}
		}
		if(!meta1.getEnchants().equals(meta2.getEnchants())) {
			return false;
		}
		if(!meta1.getItemFlags().equals(meta2.getItemFlags())) {
			return false;
		}
		if(!meta1.getLocalizedName().equals(meta2.getLocalizedName())) {
			return false;
		}
		if(meta1.getPersistentDataContainer().has(new NamespacedKey(Cookies.getPlugin(Cookies.class), "skullBlockIdentifier"), PersistentDataType.STRING) != meta2.getPersistentDataContainer().has(new NamespacedKey(Cookies.getPlugin(Cookies.class), "skullBlockIdentifier"), PersistentDataType.STRING)) {
			return false;
		}
		if(meta1.getPersistentDataContainer().has(new NamespacedKey(Cookies.getPlugin(Cookies.class), "skullBlockIdentifier"), PersistentDataType.STRING)) {
			return meta1.getPersistentDataContainer().get(new NamespacedKey(Cookies.getPlugin(Cookies.class), "skullBlockIdentifier"), PersistentDataType.STRING).equals(meta2.getPersistentDataContainer().get(new NamespacedKey(Cookies.getPlugin(Cookies.class), "skullBlockIdentifier"), PersistentDataType.STRING));
		} else {
			return true;
		}
	}

}
