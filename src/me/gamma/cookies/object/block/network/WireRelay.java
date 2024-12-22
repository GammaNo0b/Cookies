
package me.gamma.cookies.object.block.network;


import org.bukkit.block.TileState;

import me.gamma.cookies.manager.Wire;



/**
 * Allows one wire to be connected to a block of this instance.
 * 
 * @author gamma
 *
 */
public interface WireRelay extends WireHolder {

	/**
	 * Returns the wire connected to this block.
	 * 
	 * @param block the block
	 * @return the connected wire
	 */
	Wire getConnectedWire(TileState block);

	/**
	 * Sets the wire connected to this block.
	 * 
	 * @param block the block
	 * @param wire  the wire
	 */
	void setConnectedWire(TileState block, Wire wire);


	@Override
	default boolean acceptsWire(TileState block) {
		return this.getConnectedWire(block) == null;
	}


	@Override
	default void addWire(TileState block, Wire wire) {
		this.setConnectedWire(block, wire);
	}


	@Override
	default void removeWire(TileState block, Wire wire) {
		if(this.getConnectedWire(block) == wire)
			this.setConnectedWire(block, null);
	}


	@Override
	default boolean removeWire(TileState block) {
		Wire wire = this.getConnectedWire(block);
		if(wire == null)
			return false;

		this.setConnectedWire(block, null);
		wire.destroy();
		return true;
	}


	@Override
	default int removeAllWires(TileState block) {
		return this.removeWire(block) ? 1 : 0;
	}

}
