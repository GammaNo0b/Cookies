package me.gamma.cookies.objects.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;

import me.gamma.cookies.util.Utilities;

public interface Switchable {
	
	default boolean isBlockPowered(TileState block) {
		return this.isBlockPowered(block.getBlock());
	}
	
	default boolean isBlockPowered(Block block) {
		return block.getBlockPower() > 0;
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
