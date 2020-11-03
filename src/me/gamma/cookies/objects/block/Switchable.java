package me.gamma.cookies.objects.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;

import me.gamma.cookies.util.Utilities;

public interface Switchable {
	
	default boolean isBlockPowered(TileState block) {
		return block.getBlock().getBlockPower() > 0;
	}
	
	default boolean isNeighborPowered(Block block) {
		for(BlockFace face : Utilities.faces) {
			if(this.isNeighborPowered(block, face)) {
				return true;
			}
		}
		return false;
	}
	
	default boolean isNeighborPowered(Block block, BlockFace face) {
		return block.getRelative(face).getBlockPower() > 0;
	}

}
