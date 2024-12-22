
package me.gamma.cookies.object.network;


import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.block.TileState;

import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.util.collection.Pair;



public interface NetworkInterface<T> extends NetworkComponent<T> {

	/**
	 * Returns the priority of the interface at the given block.
	 * 
	 * @param block the block
	 * @return the priority
	 */
	int getPriority(TileState block);

	/**
	 * Returns the channel of this interface.
	 * 
	 * @param block the block
	 * @return the channel
	 */
	int getChannel(TileState block);

	/**
	 * Returns the list of {@link Provider} that can accept resources into the network.
	 * 
	 * @param block the block
	 * @return the list of input providers
	 */
	List<Provider<T>> getInputs(TileState block);

	/**
	 * Returns the filter deciding which resources can be accepted from this inteface into the network.
	 * 
	 * @param block the block
	 * @return the filter
	 */
	Filter<T> getInputFiler(TileState block);

	/**
	 * Returns the list of {@link Provider} that can accepts resources from the network.
	 * 
	 * @param block the block
	 * @return the list of output providers
	 */
	List<Provider<T>> getOutputs(TileState block);

	/**
	 * Returns the filter deciding which resources can be accepted to this inteface from the network.
	 * 
	 * @param block the block
	 * @return the filter
	 */
	Filter<T> getOutputFilter(TileState block);


	/**
	 * Returns a {@link Predicate} that checks if the tested interface has the same channel as the interface provided to this method.
	 * 
	 * @param <T>        the type of the network interface
	 * @param icomponent the interface
	 * @return the filter
	 */
	static <T> Predicate<Pair<TileState, NetworkInterface<T>>> createFilter(Pair<TileState, NetworkInterface<T>> icomponent) {
		final int channel = icomponent.right.getChannel(icomponent.left);
		return p -> p.right.getChannel(p.left) == channel;
	}


	/**
	 * Returns a {@link Comparator} that compares the priority of two {@link NetworkInterface}s. It will sort them decending from highes priority to
	 * lowest.
	 * 
	 * @param <T> the type of the network interface
	 * @return the comparator
	 */
	static <T> Comparator<Pair<TileState, NetworkInterface<T>>> createComparator() {
		return (p1, p2) -> p2.right.getPriority(p2.left) - p1.right.getPriority(p1.left);
	}

}
