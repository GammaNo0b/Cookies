
package me.gamma.cookies.object.tile;


import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractCustomTileEntity<T extends AbstractCustomTileEntity<T, B>, B extends AbstractCustomTileBlock<B, T>> implements CustomTileHandler {

	protected final B customBlock;
	protected final Block block;

	public AbstractCustomTileEntity(B customBlock, Block block) {
		this.customBlock = customBlock;
		this.block = block;
	}


	public B getCustomBlock() {
		return this.customBlock;
	}


	public Block getBlock() {
		return this.block;
	}


	/**
	 * Returns a correctly casted version of this tile entity.
	 * 
	 * @return this
	 */
	public abstract T castTileEntity();


	/**
	 * Returns if tile entities of this block should tick.
	 * 
	 * @return if ticking
	 */
	public boolean isTicking() {
		return false;
	}


	/**
	 * Get's executed every tick if so specified.
	 */
	public void tick() {}


	/**
	 * Loads extra data from the given data.
	 * 
	 * @param data the extra data
	 */
	public void loadFromData(PersistentDataObject data) {}


	@Override
	public boolean onTileBroken(Block block, Player player) {
		if(!CustomTileHandler.super.onTileBroken(block, player))
			return false;

		this.destroy();

		return true;
	}


	/**
	 * Destroys this tile entity.
	 */
	public void destroy() {}


	/**
	 * Loads this tile entity in the given chunk.
	 * 
	 * @param chunk the chunk
	 * @param data  the data
	 * @return if successful
	 */
	public boolean load(Chunk chunk, PersistentDataObject data) {
		return true;
	}


	/**
	 * Saves this tile entity in the given chunk.
	 * 
	 * @param chunk the chunk
	 * @param data  the data
	 * @return if successful
	 */
	public boolean save(Chunk chunk, PersistentDataObject data) {
		return true;
	}

}
