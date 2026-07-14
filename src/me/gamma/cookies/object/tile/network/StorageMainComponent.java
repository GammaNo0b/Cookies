
package me.gamma.cookies.object.tile.network;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.StorageMainComponentBlock;
import me.gamma.cookies.object.network.TransferRate;



public class StorageMainComponent<R> extends AbstractStorageMainComponent<R, StorageMainComponent<R>, StorageMainComponentBlock<R>> {

	public StorageMainComponent(StorageMainComponentBlock<R> customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public TransferRate<R> getTransferRate() {
		return this.customBlock.getTransferRate();
	}


	@Override
	public StorageMainComponent<R> castTileEntity() {
		return this;
	}

}
