
package me.gamma.cookies.object.tile.network.pipes;


import java.util.function.Predicate;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.gamma.cookies.object.block.network.pipes.FilterPipeBlock;
import me.gamma.cookies.util.BlockUtils;



public abstract class FilterPipe<P extends PipePacket, T extends FilterPipe<P, T, B>, B extends FilterPipeBlock<P, B, T>> extends Pipe<P, T, B> {

	public FilterPipe(B customBlock, Block block) {
		super(customBlock, block);
	}


	/**
	 * Returns the filter of the given direction.
	 * 
	 * @param face the direction
	 * @return the filter
	 */
	protected abstract Predicate<P> getFilter(BlockUtils.BlockFaceDirection face);


	@Override
	protected BlockFace handlePacket(P packet, BlockFace face) {
		for(BlockUtils.BlockFaceDirection direction : BlockUtils.BlockFaceDirection.values())
			if(this.getFilter(direction).test(packet))
				return direction.getFacing(this.getCustomBlock().getFacing(this.block));

		return face;
	}

}
