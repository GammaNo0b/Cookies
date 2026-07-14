
package me.gamma.cookies.object.block.network.item;


import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.network.AbstractStorageComponentBlock;
import me.gamma.cookies.object.gui.book.StorageBook;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.item.ItemStorageMonitor;



public class ItemStorageMonitorBlock extends AbstractStorageComponentBlock<ItemStack, ItemStorageMonitorBlock, ItemStorageMonitor> {

	@Override
	public String getIdentifier() {
		return "item_storage_monitor";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.STORAGE_MONITOR;
	}


	@Override
	public Class<ItemStack> getType() {
		return ItemStack.class;
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		ItemStorageMonitor monitor = this.getTileEntity(block);
		if(monitor == null)
			return true;

		if(!monitor.canAccess(player.getUniqueId())) {
			player.sendMessage("§cYou cannot access this network!");
			return true;
		}

		StorageBook.openBook(player, monitor.getNetwork());

		return true;
	}


	@Override
	public ItemStorageMonitorBlock castCustomBlock() {
		return this;
	}


	@Override
	public ItemStorageMonitor createNewTileEntity(Block block) {
		return new ItemStorageMonitor(this, block);
	}

}
