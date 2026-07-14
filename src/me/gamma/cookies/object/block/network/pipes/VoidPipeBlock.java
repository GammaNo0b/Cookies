
package me.gamma.cookies.object.block.network.pipes;


import me.gamma.cookies.object.tile.network.pipes.PipePacket;
import me.gamma.cookies.object.tile.network.pipes.VoidPipe;



public abstract class VoidPipeBlock<P extends PipePacket, B extends VoidPipeBlock<P, B, T>, T extends VoidPipe<P, T, B>> extends PipeBlock<P, B, T> {

}
