
package me.gamma.cookies.object.block.network;


import java.util.stream.Stream;

import org.bukkit.block.Block;

import me.gamma.cookies.object.tile.network.StorageConnector;
import me.gamma.cookies.util.BlockUtils;



public class StorageConnectorBlock<R> extends AbstractStorageComponentBlock<R, StorageConnectorBlock<R>, StorageConnector<R>> implements NetworkComponentBlock<R> {

	private final String identifier;
	private final String texture;
	private final Class<R> type;

	public StorageConnectorBlock(String identifier, String texture, Class<R> type) {
		this.identifier = identifier;
		this.texture = texture;
		this.type = type;
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
	public Class<R> getType() {
		return this.type;
	}


	@Override
	public Stream<Block> getPotentialNeighbors(Block block) {
		return Stream.of(BlockUtils.cartesian).mapMulti((face, consumer) -> {
			for(int i = 1; i <= 5; i++)
				consumer.accept(block.getRelative(face, i));
		});
	}


	@Override
	public StorageConnectorBlock<R> castCustomBlock() {
		return this;
	}


	@Override
	public StorageConnector<R> createNewTileEntity(Block block) {
		return new StorageConnector<>(this, block);
	}

}
