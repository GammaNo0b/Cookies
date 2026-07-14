
package me.gamma.cookies.object.tile.network.pipes;


import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.gamma.cookies.object.block.network.pipes.DirectionalPipeBlock;



public abstract class DirectionalPipe<P extends PipePacket, T extends DirectionalPipe<P, T, B>, B extends DirectionalPipeBlock<P, B, T>> extends Pipe<P, T, B> {

	public DirectionalPipe(B customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected BlockFace handlePacket(PipePacket packet, BlockFace face) {
		return this.customBlock.getFacing(this.block).getOppositeFace();
	}

}
