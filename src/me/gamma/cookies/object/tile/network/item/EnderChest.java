
package me.gamma.cookies.object.tile.network.item;


import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.network.EnderLinkedBlock;
import me.gamma.cookies.object.gui.ItemInventoryHolder;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemStorage;
import me.gamma.cookies.object.tile.network.EnderLinkedTileEntity;



public class EnderChest extends EnderLinkedTileEntity<Inventory> implements ItemStorage, ItemInventoryHolder {

	public EnderChest(EnderLinkedBlock<Inventory> customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean canAccessItemInputs(BlockFace face) {
		return true;
	}


	@Override
	public byte getItemInputAccessFlags() {
		return 0x3F;
	}


	@Override
	public void setItemInputAccessFlags(byte flags) {}


	@Override
	public boolean canAccessItemOutputs(BlockFace face) {
		return true;
	}


	@Override
	public byte getItemOutputAccessFlags() {
		return 0x3F;
	}


	@Override
	public void setItemOutputAccessFlags(byte flags) {}


	@Override
	public List<Provider<ItemStack>> getItemProviders() {
		return ItemProvider.fromInventory(this.getResource());
	}


	@Override
	public Inventory getInventory() {
		return this.getResource();
	}


	@Override
	public int[] getInputSlots() {
		return null;
	}


	@Override
	public int[] getOutputSlots() {
		return null;
	}

}
