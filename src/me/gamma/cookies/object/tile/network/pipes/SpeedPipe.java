
package me.gamma.cookies.object.tile.network.pipes;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.pipes.SpeedPipeBlock;



public abstract class SpeedPipe<P extends PipePacket, T extends SpeedPipe<P, T, B>, B extends SpeedPipeBlock<P, B, T>> extends Pipe<P, T, B> {

	public SpeedPipe(B customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected double getPacketSpeed() {
		return this.customBlock.getPacketSpeed();
	}

}
