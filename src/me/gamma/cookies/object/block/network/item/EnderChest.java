
package me.gamma.cookies.object.block.network.item;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.network.EnderLinkedBlock;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemStorage;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.ListProperty;
import me.gamma.cookies.util.ItemUtils;



public class EnderChest extends EnderLinkedBlock<Inventory> implements ItemStorage {

	private static final ListProperty<ItemStack, ItemStackProperty> INVENTORY = new ListProperty<>("inventory", ItemStackProperty::new);

	@Override
	protected Inventory newResource() {
		return Bukkit.createInventory(null, 27, "Ender Chest");
	}


	@Override
	protected void loadResource(Inventory resource, PersistentDataContainer container) {
		List<ItemStack> items = INVENTORY.fetch(container);
		int m = Math.min(items.size(), resource.getSize());
		for(int i = 0; i < m; i++)
			resource.setItem(i, items.get(i));
	}


	@Override
	protected boolean saveResource(Inventory resource, PersistentDataContainer container) {
		if(resource.isEmpty())
			return false;

		ArrayList<ItemStack> items = new ArrayList<>(resource.getSize());
		for(int i = 0; i < resource.getSize(); i++) {
			ItemStack item = resource.getItem(i);
			if(!ItemUtils.isEmpty(item))
				items.add(item);
		}

		INVENTORY.store(container, items);
		return true;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ENDER_CHEST;
	}


	@Override
	public String getIdentifier() {
		return "ender_chest";
	}


	@Override
	public byte getItemInputAccessFlags(PersistentDataHolder holder) {
		return 0x3F;
	}


	@Override
	public byte getItemOutputAccessFlags(PersistentDataHolder holder) {
		return 0x3f;
	}


	@Override
	public List<Provider<ItemStack>> getItemProviders(PersistentDataHolder holder) {
		return ItemProvider.fromInventory(this.getResource(holder));
	}


	@Override
	protected void displayResources(Player player, TileState block, Inventory resource) {
		player.playSound(block.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0F, 1.0F);
		player.openInventory(resource);
	}

}
