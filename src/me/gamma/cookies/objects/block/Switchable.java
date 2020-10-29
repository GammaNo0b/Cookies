package me.gamma.cookies.objects.block;

import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;

import me.gamma.cookies.util.Utilities;

public interface Switchable {
	
	boolean isActiveOnRedstone();
	
	default boolean isBlockPowered(TileState block) {
		return block.getBlock().getBlockPower() > 0;
	}
	
	default boolean isNeighborPowered(TileState block) {
		for(BlockFace face : Utilities.faces) {
			if(this.isNeighborPowered(block, face)) {
				return true;
			}
		}
		return false;
	}
	
	default boolean isNeighborPowered(TileState block, BlockFace face) {
		return block.getBlock().getRelative(face).getBlockPower() > 0 == this.isActiveOnRedstone();
	}

}
