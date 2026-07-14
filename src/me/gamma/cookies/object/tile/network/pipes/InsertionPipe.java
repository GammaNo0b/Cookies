
package me.gamma.cookies.object.tile.network.pipes;


import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.gamma.cookies.object.block.network.pipes.InsertionPipeBlock;



public abstract class InsertionPipe<P extends PipePacket, T extends InsertionPipe<P, T, B>, B extends InsertionPipeBlock<P, B, T>> extends Pipe<P, T, B> {

	public InsertionPipe(B customBlock, Block block) {
		super(customBlock, block);
	}


	/**
	 * Inserts the resources in the given packet into the given block.
	 * 
	 * @param packet the resoruce packet
	 * @param target the target block
	 * @return whether successful
	 */
	protected abstract boolean insertResource(P packet, Block target);


	@Override
	protected BlockFace handlePacket(P packet, BlockFace face) {
		return this.customBlock.getFacing(this.block).getOppositeFace();
	}


	@Override
	protected void exitPacket(P packet, BlockFace face) {
		if(this.insertResource(packet, this.block.getRelative(face))) {
			packet.remove();
			return;
		}

		super.exitPacket(packet, face);
	}

}
