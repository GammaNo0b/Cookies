
package me.gamma.cookies.object.block.network.item;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.gui.book.StorageBook;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.network.NetworkMainComponent;
import me.gamma.cookies.object.network.TransferRate;



public class StorageMonitor extends AbstractCustomBlock implements NetworkMainComponent<ItemStack> {

	private final Set<Location> components = new HashSet<>();

	@Override
	public String getBlockTexture() {
		return HeadTextures.STORAGE_MONITOR;
	}


	@Override
	public String getIdentifier() {
		return "storage_monitor";
	}


	@Override
	public Set<Location> getLocations() {
		return this.components;
	}


	@Override
	public Class<ItemStack> getType() {
		return ItemStack.class;
	}


	@Override
	public TransferRate<ItemStack> getTransferRate() {
		return ItemStack::getMaxStackSize;
	}


	@Override
	public void breakComponent(TileState block) {
		// Bukkit.getPluginManager().callEvent(new BlockBreakEvent(block.getBlock(), this.getOwningPlayer(block)));
		block.setType(Material.AIR);
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
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(super.onBlockBreak(player, block, event))
			return true;
		
		this.destroy(block);
		return false;
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(!this.canAccess(block, player))
			return false;

		StorageBook.openBook(player, this.getNetwork(block));
		return true;
	}

}
