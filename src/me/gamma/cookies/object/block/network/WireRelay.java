
package me.gamma.cookies.object.block.network;


import org.bukkit.block.Block;



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
	 * @return the connected wire
	 */
	Wire<T> getConnectedWire();

	/**
	 * Sets the wire connected to this block.
	 * 
	 * @param wire the wire
	 */
	void setConnectedWire(Wire<T> wire);


	@Override
	default boolean acceptsWire() {
		return this.getConnectedWire() == null;
	}


	@Override
	default void addWire(Wire<T> wire) {
		this.setConnectedWire(wire);
	}


	@Override
	default void removeWire(Wire<T> wire) {
		if(this.getConnectedWire() == wire)
			this.setConnectedWire(null);
	}


	@Override
	default Wire<T> removeWire() {
		Wire<T> wire = this.getConnectedWire();
		if(wire != null) {
			this.setConnectedWire(null);
			wire.destroy();
		}
		return wire;
	}


	@Override
	default void removeWires() {
		this.removeWire();
	}


	@Override
	default boolean hasWireTo(Block destination) {
		Wire<T> wire = this.getConnectedWire();
		return wire != null && wire.isConnectedTo(destination.getLocation());
	}

}
