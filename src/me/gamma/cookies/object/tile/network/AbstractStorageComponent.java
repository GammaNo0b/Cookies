
package me.gamma.cookies.object.tile.network;


import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.gamma.cookies.object.block.network.AbstractStorageComponentBlock;
import me.gamma.cookies.object.block.network.NetworkComponent;
import me.gamma.cookies.object.block.network.NetworkComponentBlock;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractStorageComponent<R, T extends AbstractStorageComponent<R, T, B>, B extends AbstractStorageComponentBlock<R, B, T>> extends AbstractCustomTileEntity<T, B> implements NetworkComponent<R> {

	private static final String KEY_NETWORK_ID = "networkid";

	private Integer networkID;

	public AbstractStorageComponent(B customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.networkID = data.getInteger(KEY_NETWORK_ID);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		if(this.networkID != null)
			data.setInteger(KEY_NETWORK_ID, this.networkID);

		return true;
	}


	@Override
	public boolean onTileCreated(Block block, Player player) {
		if(!super.onTileCreated(block, player))
			return false;

		if(!this.setup(player.getUniqueId()))
			return false;

		return true;
	}


	@Override
	public void destroy() {
		super.destroy();

		NetworkComponent.super.destroy();
	}


	@Override
	public void setNetworkID(Integer id) {
		this.networkID = id;
	}


	@Override
	public Integer getNetworkID() {
		return this.networkID;
	}


	@Override
	public NetworkComponentBlock<R> getComponentBlock() {
		return this.customBlock;
	}

}
