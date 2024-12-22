
package me.gamma.cookies.object.block;


import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;

import me.gamma.cookies.util.BlockUtils;



public interface Switchable {


	default boolean isBlockPowered(Block block) {
		return block.getBlockPower() > 0;
	}

	default boolean isBlockPowered(TileState block) {
		return this.isBlockPowered(block.getBlock());
	}


	default boolean isNeighborsPowered(Block block) {
		for(BlockFace face : BlockUtils.cartesian)
			if(this.isNeighborPowered(block, face))
				return true;
		return false;
	}


	default boolean isNeighborPowered(Block block, BlockFace face) {
		return this.isBlockPowered(block.getRelative(face));
	}

}
