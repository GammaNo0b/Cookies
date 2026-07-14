
package me.gamma.cookies.object.tile.network;


import org.bukkit.Chunk;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.AbstractStorageMainComponentBlock;
import me.gamma.cookies.object.block.network.NetworkMainComponentBlock;
import me.gamma.cookies.object.network.Network;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractStorageMainComponent<R, T extends AbstractStorageMainComponent<R, T, B>, B extends AbstractStorageMainComponentBlock<R, B, T>> extends AbstractStorageComponent<R, T, B> implements NetworkMainComponent<R> {

	private static final String KEY_LAST_UPDATE = "lastupdate";

	private long lastUpdate = 0L;

	public AbstractStorageMainComponent(B customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.lastUpdate = data.getLong(KEY_LAST_UPDATE, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		data.setLong(KEY_LAST_UPDATE, this.lastUpdate);

		return true;
	}


	@Override
	public void destroy() {
		NetworkMainComponent.super.destroy();
	}


	@Override
	public NetworkMainComponentBlock<R> getComponentBlock() {
		return this.customBlock;
	}


	public void highlight() {
		Network<R> network = this.getNetwork();
		if(network != null)
			network.highlightNetwork();
	}

}
