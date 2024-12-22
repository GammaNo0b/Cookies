
package me.gamma.cookies.object.block;


import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;

import me.gamma.cookies.util.BlockUtils;



/**
 * {@link AbstractCustomBlock}s implementing this interface will always be facing in a cartesian direction.
 * 
 * @see {@link BlockFace#isCartesian()}
 */
public interface Cartesian {

	/**
	 * Returns if this block should correct it's facing on placement.
	 * 
	 * @param block the block
	 * @return if the facing should be corrected
	 */
	default boolean shouldCorrectFacing(TileState block) {
		return true;
	}


	/**
	 * Returns the direction this block is facing into.
	 * 
	 * @param block the block
	 * @return the facing direction
	 */
	default BlockFace getFacing(TileState block) {
		return BlockUtils.getFacing(block.getBlockData());
	}


	/**
	 * Corrects the direction of this block if it is not facing in a cartesian direction.
	 * 
	 * @param block the block
	 */
	default void correctFacing(TileState block) {
		BlockUtils.getSetFacing(block.getBlock(), BlockUtils::getClosestCartesianFacing);
	}

}
