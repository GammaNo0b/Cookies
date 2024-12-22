
package me.gamma.cookies.object.block.network.item;


import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.network.NetworkComponent;



public class StorageConnector extends AbstractCustomBlock implements NetworkComponent<ItemStack> {

	@Override
	public Class<ItemStack> getType() {
		return ItemStack.class;
	}


	@Override
	public String getIdentifier() {
		return "storage_connector";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.STORAGE_CONNECTOR;
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

}
