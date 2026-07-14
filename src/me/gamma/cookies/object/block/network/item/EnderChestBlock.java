
package me.gamma.cookies.object.block.network.item;


import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.block.network.EnderLinkedBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.EnderLinkedTileEntity;
import me.gamma.cookies.object.tile.network.item.EnderChest;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class EnderChestBlock extends EnderLinkedBlock<Inventory> {

	@Override
	protected Inventory newResource() {
		return Bukkit.createInventory(null, 27, "Ender Chest");
	}


	@Override
	protected boolean loadResource(Inventory resource, PersistentDataObject data) {
		return PersistentDataUtils.loadInventory(data, resource) != null;
	}


	@Override
	protected boolean saveResource(Inventory resource, PersistentDataObject data) {
		PersistentDataUtils.saveInventory(data, resource, "");
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
	protected void displayResources(Player player, Inventory resource) {
		player.playSound(player, Sound.BLOCK_ENDER_CHEST_OPEN, 1.0F, 1.0F);
		player.openInventory(resource);
	}


	@Override
	public EnderLinkedBlock<Inventory> castCustomBlock() {
		return this;
	}


	@Override
	public EnderLinkedTileEntity<Inventory> createNewTileEntity(Block block) {
		return new EnderChest(this, block);
	}

}
