
package me.gamma.cookies.object.tile.network;


import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.block.Block;

import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.network.NetworkComponent;
import me.gamma.cookies.object.block.network.NetworkInterfaceBlock;
import me.gamma.cookies.util.collection.Pair;



public interface NetworkInterface<T> extends NetworkComponent<T> {

	@Override
	NetworkInterfaceBlock<T> getComponentBlock();

	/**
	 * Returns the priority of the interface of this block.
	 * 
	 * @return the priority
	 */
	int getPriority();

	/**
	 * Returns the channel of this interface.
	 * 
	 * @return the channel
	 */
	int getChannel();

	/**
	 * Returns the list of {@link Provider} that can accept resources into the network.
	 * 
	 * @return the list of input providers
	 */
	List<Provider<T>> getInputs();

	/**
	 * Returns the filter deciding which resources can be accepted from this inteface into the network.
	 * 
	 * @return the filter
	 */
	Filter<T> getInputFiler();

	/**
	 * Returns the list of {@link Provider} that can accepts resources from the network.
	 * 
	 * @return the list of output providers
	 */
	List<Provider<T>> getOutputs();

	/**
	 * Returns the filter deciding which resources can be accepted to this inteface from the network.
	 * 
	 * @return the filter
	 */
	Filter<T> getOutputFilter();


	/**
	 * Returns a {@link Predicate} that checks if the tested interface has the same channel as the interface provided to this method.
	 * 
	 * @param <T>        the type of the network interface
	 * @param icomponent the interface
	 * @return the filter
	 */
	static <T> Predicate<Pair<Block, NetworkInterface<T>>> createFilter(Pair<Block, NetworkInterface<T>> icomponent) {
		final int channel = icomponent.right.getChannel();
		return p -> p.right.getChannel() == channel;
	}


	/**
	 * Returns a {@link Comparator} that compares the priority of two {@link NetworkInterface}s. It will sort them decending from highes priority to
	 * lowest.
	 * 
	 * @param <T> the type of the network interface
	 * @return the comparator
	 */
	static <T> Comparator<Pair<Block, NetworkInterface<T>>> createComparator() {
		return (p1, p2) -> p2.right.getPriority() - p1.right.getPriority();
	}

}
