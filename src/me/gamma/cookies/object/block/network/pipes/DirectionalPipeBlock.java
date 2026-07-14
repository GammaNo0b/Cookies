
package me.gamma.cookies.object.block.network.pipes;


import me.gamma.cookies.object.tile.network.pipes.DirectionalPipe;
import me.gamma.cookies.object.tile.network.pipes.PipePacket;



public abstract class DirectionalPipeBlock<P extends PipePacket, B extends DirectionalPipeBlock<P, B, T>, T extends DirectionalPipe<P, T, B>> extends PipeBlock<P, B, T> {

}
