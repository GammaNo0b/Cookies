
package me.gamma.cookies.object.block.network;


import java.util.List;

import org.bukkit.block.TileState;



/**
 * Allows multiple wires to be connected to this block.
 * 
 * @author gamma
 *
 */
public interface WireConnector<T> extends WireHolder<T> {

	/**
	 * Returns a list containing all wires connected to the given block.
	 * 
	 * @param block the block
	 * @return the list of connected wires
	 */
	List<Wire<T>> getConnectedWires(TileState block);

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
	default void addWire(TileState block, Wire<T> wire) {
		this.getConnectedWires(block).add(wire);
	}


	@Override
	default void removeWire(TileState block, Wire<T> wire) {
		this.getConnectedWires(block).remove(wire);
	}


	@Override
	default Wire<T> removeWire(TileState block) {
		List<Wire<T>> wires = this.getConnectedWires(block);
		if(wires.isEmpty())
			return null;

		Wire<T> wire = wires.remove(wires.size() - 1);
		wire.destroy();
		return wire;
	}



}
