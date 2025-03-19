
package me.gamma.cookies.object.block.network.item;


import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.gui.book.StorageBook;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.network.NetworkComponent;



public class StorageMonitor extends AbstractCustomBlock implements NetworkComponent<ItemStack> {

	@Override
	public String getIdentifier() {
		return "storage_monitor";
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
	public boolean canPlace(Player player, Block block) {
		if(player == null)
			return false;

		if(this.checkForAdjacentNotOwnedNetworks(player.getUniqueId(), block.getLocation()))
			return false;

		return super.canPlace(player, block);
	}


	@Override
	public boolean onBlockPlace(Player player, PersistentDataHolder holder, TileState block) {
		if(super.onBlockPlace(player, holder, block))
			return true;

		if(player == null)
			return true;

		return !this.setup(block, player.getUniqueId());
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(!this.canAccess(block, player.getUniqueId()))
			return false;

		StorageBook.openBook(player, this.getNetwork(block));
		return true;
	}

}
