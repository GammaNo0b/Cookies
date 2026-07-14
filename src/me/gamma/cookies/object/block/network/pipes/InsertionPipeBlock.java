
package me.gamma.cookies.object.block.network.pipes;


import me.gamma.cookies.object.tile.network.pipes.InsertionPipe;
import me.gamma.cookies.object.tile.network.pipes.PipePacket;



public abstract class InsertionPipeBlock<P extends PipePacket, B extends InsertionPipeBlock<P, B, T>, T extends InsertionPipe<P, T, B>> extends PipeBlock<P, B, T> {

}
