
package me.gamma.cookies.object.block.network.pipes;


import me.gamma.cookies.object.tile.network.pipes.ExtractionPipe;
import me.gamma.cookies.object.tile.network.pipes.PipePacket;



public abstract class ExtractionPipeBlock<P extends PipePacket, B extends ExtractionPipeBlock<P, B, T>, T extends ExtractionPipe<P, T, B>> extends PipeBlock<P, B, T> {

	/**
	 * Number of ticks required per extraction.
	 * 
	 * @return the ticks
	 */
	public abstract int extractionTicks();

}
