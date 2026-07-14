
package me.gamma.cookies.object.item.resources;


import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class MagicMetal extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "magic_metal";
	}


	@Override
	public String getTitle() {
		return "§bMagic Metal";
	}


	@Override
	public Material getMaterial() {
		return Material.GOLD_INGOT;
	}


	@Override
	protected void editItemMeta(ItemMeta meta, PersistentDataObject data) {
		super.editItemMeta(meta, data);

		meta.addEnchant(Enchantment.PROTECTION, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	}

}
