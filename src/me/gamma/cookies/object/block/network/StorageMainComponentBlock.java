
package me.gamma.cookies.object.block.network;


import org.bukkit.block.Block;

import me.gamma.cookies.object.network.TransferRate;
import me.gamma.cookies.object.tile.network.StorageMainComponent;



public class StorageMainComponentBlock<R> extends AbstractStorageMainComponentBlock<R, StorageMainComponentBlock<R>, StorageMainComponent<R>> implements NetworkMainComponentBlock<R> {

	private final String identifier;
	private final String texture;
	private final TransferRate<R> transferRate;

	public StorageMainComponentBlock(String identifier, String texture, TransferRate<R> transferRate, Class<R> type) {
		super(type);

		this.identifier = identifier;
		this.texture = texture;
		this.transferRate = transferRate;
	}


	public TransferRate<R> getTransferRate() {
		return this.transferRate;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public String getBlockTexture() {
		return this.texture;
	}


	@Override
	public StorageMainComponentBlock<R> castCustomBlock() {
		return this;
	}


	@Override
	public StorageMainComponent<R> createNewTileEntity(Block block) {
		return new StorageMainComponent<>(this, block);
	}

}
