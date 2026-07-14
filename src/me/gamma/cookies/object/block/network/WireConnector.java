
package me.gamma.cookies.object.block.network;


import java.util.List;

import org.bukkit.block.Block;



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
	 * @return the list of connected wires
	 */
	List<Wire<T>> getConnectedWires();

	/**
	 * Returns the maximum amount of wires this block can connect to.
	 * 
	 * @return the maximum amount of wires
	 */
	int getMaximumWireCount();


	@Override
	default boolean acceptsWire() {
		return this.getConnectedWires().size() < this.getMaximumWireCount();
	}


	@Override
	default void addWire(Wire<T> wire) {
		this.getConnectedWires().add(wire);
	}


	@Override
	default void removeWire(Wire<T> wire) {
		this.getConnectedWires().remove(wire);
	}


	@Override
	default Wire<T> removeWire() {
		List<Wire<T>> wires = this.getConnectedWires();
		if(wires.isEmpty())
			return null;

		Wire<T> wire = wires.remove(wires.size() - 1);
		wire.destroy();
		return wire;
	}


	@Override
	default void removeWires() {
		List<Wire<T>> wires = this.getConnectedWires();
		while(!wires.isEmpty())
			wires.get(0).destroy();

		wires.clear();
	}


	@Override
	default boolean hasWireTo(Block destination) {
		for(Wire<T> wire : this.getConnectedWires())
			if(wire.isConnectedTo(destination.getLocation()))
				return true;

		return false;
	}

}
