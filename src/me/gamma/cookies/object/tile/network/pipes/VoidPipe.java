
package me.gamma.cookies.object.tile.network.pipes;


import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.gamma.cookies.object.block.network.pipes.VoidPipeBlock;



public abstract class VoidPipe<P extends PipePacket, T extends VoidPipe<P, T, B>, B extends VoidPipeBlock<P, B, T>> extends Pipe<P, T, B> {

	public VoidPipe(B customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected BlockFace handlePacket(P packet, BlockFace face) {
		return BlockFace.SELF;
	}

}
