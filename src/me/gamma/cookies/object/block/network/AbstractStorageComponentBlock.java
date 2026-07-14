
package me.gamma.cookies.object.block.network;


import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.object.tile.network.AbstractStorageComponent;



public abstract class AbstractStorageComponentBlock<R, B extends AbstractStorageComponentBlock<R, B, T>, T extends AbstractStorageComponent<R, T, B>> extends AbstractCustomTileBlock<B, T> implements NetworkComponentBlock<R> {

	@Override
	public boolean canPlace(Player player, Block block) {
		if(player == null)
			return false;

		if(!super.canPlace(player, block))
			return false;

		if(this.checkForAdjacentNotOwnedNetworks(player.getUniqueId(), block))
			return false;

		return true;
	}

}
