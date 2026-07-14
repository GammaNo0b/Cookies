
package me.gamma.cookies.object.block.network.pipes;


import me.gamma.cookies.object.tile.network.pipes.FilterPipe;
import me.gamma.cookies.object.tile.network.pipes.PipePacket;



public abstract class FilterPipeBlock<P extends PipePacket, B extends FilterPipeBlock<P, B, T>, T extends FilterPipe<P, T, B>> extends PipeBlock<P, B, T> {

}
