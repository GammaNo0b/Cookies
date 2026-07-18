
package me.gamma.cookies.object.tile.network.item;


import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.network.item.TrashcanBlock;
import me.gamma.cookies.object.gui.EmptyContainer;
import me.gamma.cookies.object.gui.ItemInventoryHolder;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;



public class Trashcan extends AbstractCustomTileEntity<Trashcan, TrashcanBlock> implements ItemConsumer, BlockInventoryHolder, ItemInventoryHolder {

	public Trashcan(TrashcanBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public List<Provider<ItemStack>> getItemInputs() {
		return List.of(new ItemProvider() {

			@Override
			public void setType(ItemStack type) {}


			@Override
			public void remove(int amount) {}


			@Override
			public ItemStack getType() {
				return null;
			}


			@Override
			public int capacity() {
				return Integer.MAX_VALUE;
			}


			@Override
			public boolean canChangeType(ItemStack type) {
				return true;
			}


			@Override
			public int amount() {
				return 0;
			}


			@Override
			public void add(ItemStack type, int amount) {}

		});
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
	public Trashcan castTileEntity() {
		return this;
	}


	@Override
	public Inventory getInventory() {
		return new CraftInventory(new EmptyContainer(this));
	}


	@Override
	public int[] getInputSlots() {
		return null;
	}


	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}

}
