
package me.gamma.cookies.object.block.redstone;


import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;

import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.collection.TertiaryOperator;



public class TripleInputRedstoneGate extends AbstractRedstoneGate {

	private final String identifier;
	private final TertiaryOperator<Boolean> operation;

	public TripleInputRedstoneGate(String identifier, TertiaryOperator<Boolean> operation) {
		this.identifier = identifier;
		this.operation = operation;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public boolean calculateOutput(Skull block, BlockFace facing) {
		return this.operation.apply(this.isNeighborPowered(block.getBlock(), BlockUtils.rotateYClockwise(facing)), this.isNeighborPowered(block.getBlock(), facing), this.isNeighborPowered(block.getBlock(), BlockUtils.rotateYCounterClockwise(facing)));
	}

}
