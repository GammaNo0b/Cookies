
package me.gamma.cookies.object.item;


import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class CustomItemData {

	private static final NamespacedKey KEY_CUSTOM_ITEM_DATA = new NamespacedKey(Cookies.INSTANCE, "customdata");

	public static void initCustomData(ItemMeta meta, PersistentDataObject data) {
		meta.getPersistentDataContainer().set(KEY_CUSTOM_ITEM_DATA, PersistentDataType.TAG_CONTAINER, data.getContainer());
	}


	public static CustomItemData getCustomItemData(ItemStack stack) {
		if(ItemUtils.isEmpty(stack))
			return null;

		ItemMeta meta = stack.getItemMeta();
		if(meta == null)
			return null;

		PersistentDataContainer container = meta.getPersistentDataContainer().get(KEY_CUSTOM_ITEM_DATA, PersistentDataType.TAG_CONTAINER);
		if(container == null)
			return null;

		return new CustomItemData(stack, new PersistentDataObject(container));
	}

	private final ItemStack stack;
	private final PersistentDataObject data;

	private CustomItemData(ItemStack stack, PersistentDataObject data) {
		this.stack = stack;
		this.data = data;
	}


	public PersistentDataObject getData() {
		return this.data;
	}


	public void save(ItemMeta meta) {
		meta.getPersistentDataContainer().set(KEY_CUSTOM_ITEM_DATA, PersistentDataType.TAG_CONTAINER, this.data.getContainer());
	}


	public void save() {
		ItemMeta meta = this.stack.getItemMeta();
		this.save(meta);
		this.stack.setItemMeta(meta);
	}

}
