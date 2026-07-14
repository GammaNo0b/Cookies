
package me.gamma.cookies.object.block.network.pipes;


import me.gamma.cookies.object.tile.network.pipes.PipePacket;
import me.gamma.cookies.object.tile.network.pipes.VacuumPipe;



public abstract class VacuumPipeBlock<P extends PipePacket, B extends VacuumPipeBlock<P, B, T>, T extends VacuumPipe<P, T, B>> extends PipeBlock<P, B, T> {

}
