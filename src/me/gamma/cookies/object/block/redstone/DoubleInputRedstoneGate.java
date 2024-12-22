
package me.gamma.cookies.object.block.redstone;


import java.util.function.BinaryOperator;

import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;

import me.gamma.cookies.util.BlockUtils;



public class DoubleInputRedstoneGate extends AbstractRedstoneGate {

	private final String identifier;
	private final BinaryOperator<Boolean> operation;

	public DoubleInputRedstoneGate(String identifier, BinaryOperator<Boolean> operation) {
		this.identifier = identifier;
		this.operation = operation;
	}
	
	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public boolean calculateOutput(Skull block, BlockFace facing) {
		return this.operation.apply(this.isNeighborPowered(block.getBlock(), BlockUtils.rotateYClockwise(facing)), this.isNeighborPowered(block.getBlock(), BlockUtils.rotateYCounterClockwise(facing)));
	}

}
