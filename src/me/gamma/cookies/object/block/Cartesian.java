
package me.gamma.cookies.object.block;


import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.gamma.cookies.util.BlockUtils;



/**
 * {@link AbstractCustomBlock}s implementing this interface will always be facing in a cartesian direction.
 * 
 * @see {@link BlockFace#isCartesian()}
 */
public interface Cartesian {

	/**
	 * Returns if the given block should correct it's facing on placement.
	 * 
	 * @param block the block
	 * @return if the facing should be corrected
	 */
	default boolean shouldCorrectFacing(Block block) {
		return true;
	}


	/**
	 * Returns the direction the given block is facing into.
	 * 
	 * @param block the block
	 * @return the facing direction
	 */
	default BlockFace getFacing(Block block) {
		return BlockUtils.getFacing(block.getBlockData());
	}


	/**
	 * Corrects the direction of the given block if it is not facing in a cartesian direction.
	 * 
	 * @param block the block
	 */
	default void correctFacing(Block block) {
		BlockUtils.getSetFacing(block, BlockUtils::getClosestCartesianFacing);
	}

}
