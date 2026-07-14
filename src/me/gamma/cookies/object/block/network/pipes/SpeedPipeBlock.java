
package me.gamma.cookies.object.block.network.pipes;


import me.gamma.cookies.object.tile.network.pipes.PipePacket;
import me.gamma.cookies.object.tile.network.pipes.SpeedPipe;



public abstract class SpeedPipeBlock<P extends PipePacket, B extends SpeedPipeBlock<P, B, T>, T extends SpeedPipe<P, T, B>> extends PipeBlock<P, B, T> {

	/**
	 * Returns the speed of the packet.
	 * 
	 * Unit is packets / second.
	 * 
	 * @return the speed
	 */
	public abstract double getPacketSpeed();

}
