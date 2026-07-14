
package me.gamma.cookies.object.tile;


import org.bukkit.block.Block;
import org.bukkit.entity.Player;



public interface CustomTileHandler {

	/**
	 * Is called right after this tile entity has been created and loaded.
	 * 
	 * @param block  the block
	 * @param player the player
	 * @return if the placement was successful
	 */
	default boolean onTileCreated(Block block, Player player) {
		return true;
	}


	/**
	 * Get's called when a tile block should be broken.
	 * 
	 * @param block  the block
	 * @param player the player
	 * @return if the breaking was successful
	 */
	default boolean onTileBroken(Block block, Player player) {
		return true;
	}

}
