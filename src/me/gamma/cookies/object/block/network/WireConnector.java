
package me.gamma.cookies.object.block.network;


import java.util.List;

import org.bukkit.block.TileState;

import me.gamma.cookies.manager.Wire;



/**
 * Allows multiple wires to be connected to this block.
 * 
 * @author gamma
 *
 */
public interface WireConnector extends WireHolder {

	/**
	 * Returns a list containing all wires connected to the given block.
	 * 
	 * @param block the block
	 * @return the list of connected wires
	 */
	List<Wire> getConnectedWires(TileState block);

	/**
	 * Returns the maximum amount of wires this block can connect to.
	 * 
	 * @param block the block
	 * @return the maximum amount of wires
	 */
	int getMaximumWireCount(TileState block);


	@Override
	default boolean acceptsWire(TileState block) {
		return this.getConnectedWires(block).size() < this.getMaximumWireCount(block);
	}


	@Override
	default void addWire(TileState block, Wire wire) {
		this.getConnectedWires(block).add(wire);
	}


	@Override
	default void removeWire(TileState block, Wire wire) {
		this.getConnectedWires(block).remove(wire);
	}


	@Override
	default boolean removeWire(TileState block) {
		List<Wire> wires = this.getConnectedWires(block);
		if(wires.isEmpty())
			return false;

		wires.remove(wires.size() - 1).destroy();
		return true;
	}


	@Override
	default int removeAllWires(TileState block) {
		List<Wire> wires = this.getConnectedWires(block);
		int size = wires.size();
		while(!wires.isEmpty())
			wires.get(0).destroy();
		return size;
	}

}
