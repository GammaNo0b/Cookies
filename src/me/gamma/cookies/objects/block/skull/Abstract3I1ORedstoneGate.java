package me.gamma.cookies.objects.block.skull;

import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;

import me.gamma.cookies.util.Utilities;


public abstract class Abstract3I1ORedstoneGate extends AbstractRedstoneGate {

	@Override
	public boolean calculateOutput(Skull block, BlockFace facing) {
		return this.calculateOutput(this.isNeighborPowered(block.getBlock(), Utilities.rotateYClockwise(facing)), this.isNeighborPowered(block.getBlock(), facing), this.isNeighborPowered(block.getBlock(), Utilities.rotateYCounterClockwise(facing)));
	}
	
	protected abstract boolean calculateOutput(boolean left, boolean middle, boolean right);

}
