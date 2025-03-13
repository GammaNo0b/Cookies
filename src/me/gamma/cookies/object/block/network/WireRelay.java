
package me.gamma.cookies.object.block.network;


import org.bukkit.block.TileState;



/**
 * Allows one wire to be connected to a block of this instance.
 * 
 * @author gamma
 *
 */
public interface WireRelay<T> extends WireHolder<T> {

	/**
	 * Returns the wire connected to this block.
	 * 
	 * @param block the block
	 * @return the connected wire
	 */
	Wire<T> getConnectedWire(TileState block);

	/**
	 * Sets the wire connected to this block.
	 * 
	 * @param block the block
	 * @param wire  the wire
	 */
	void setConnectedWire(TileState block, Wire<T> wire);


	@Override
	default boolean acceptsWire(TileState block) {
		return this.getConnectedWire(block) == null;
	}


	@Override
	default void addWire(TileState block, Wire<T> wire) {
		this.setConnectedWire(block, wire);
	}


	@Override
	default void removeWire(TileState block, Wire<T> wire) {
		if(this.getConnectedWire(block) == wire)
			this.setConnectedWire(block, null);
	}


	@Override
	default Wire<T> removeWire(TileState block) {
		Wire<T> wire = this.getConnectedWire(block);
		if(wire != null) {
			this.setConnectedWire(block, null);
			wire.destroy();
		}
		return wire;
	}

}
