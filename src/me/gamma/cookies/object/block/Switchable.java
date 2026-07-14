
package me.gamma.cookies.object.block;


import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.gamma.cookies.util.BlockUtils;



/**
 * 
 * Represents blocks that can be controlled via redstone.
 * 
 */
public interface Switchable {

	/**
	 * Returns the redstone mode.
	 * 
	 * @return the mode
	 */
	RedstoneMode getRedstoneMode();


	/**
	 * Returns if this switchable is active.
	 * 
	 * @return if active
	 */
	default boolean isActive() {
		return this.getRedstoneMode().isActive(this.isBlockPowered());
	}


	/**
	 * Returns the block.
	 * 
	 * @return the block
	 */
	Block getBlock();


	/**
	 * Checks if powered.
	 * 
	 * @return if powered
	 */
	default boolean isBlockPowered() {
		return this.getBlock().getBlockPower() > 0;
	}


	/**
	 * Checks if the neighbor of this block on the given block face is powered.
	 * 
	 * @param face the block face
	 * @return if powered
	 */
	default boolean isNeighborPowered(BlockFace face) {
		return this.getBlock().getRelative(face).getBlockPower() > 0;
	}


	/**
	 * Checks if any of the neighbors of this block are powered.
	 * 
	 * @return if powered
	 */
	default boolean isNeighborsPowered() {
		for(BlockFace face : BlockUtils.cartesian)
			if(this.isNeighborPowered(face))
				return true;
		return false;
	}

}
