
package me.gamma.cookies.object.tile.network.item;


import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.network.item.ItemStorageMonitorBlock;
import me.gamma.cookies.object.tile.network.AbstractStorageComponent;



public class ItemStorageMonitor extends AbstractStorageComponent<ItemStack, ItemStorageMonitor, ItemStorageMonitorBlock> {

	public ItemStorageMonitor(ItemStorageMonitorBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public ItemStorageMonitor castTileEntity() {
		return this;
	}

}
