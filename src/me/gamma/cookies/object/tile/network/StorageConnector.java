
package me.gamma.cookies.object.tile.network;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.StorageConnectorBlock;



public class StorageConnector<R> extends AbstractStorageComponent<R, StorageConnector<R>, StorageConnectorBlock<R>> {

	public StorageConnector(StorageConnectorBlock<R> customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public StorageConnector<R> castTileEntity() {
		return this;
	}

}
