
package me.gamma.cookies.object.block.network;


import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.bukkit.block.Block;

import me.gamma.cookies.object.network.Network;
import me.gamma.cookies.util.BlockUtils;



public interface NetworkComponentBlock<T> {

	/**
	 * Returns the type class.
	 * 
	 * @return the type class
	 */
	Class<T> getType();


	/**
	 * Returns a stream containing all the neighbors of the given block.
	 * 
	 * @param block the block
	 * @return the stream of neighbors
	 */
	default Stream<Block> getPotentialNeighbors(Block block) {
		return Stream.of(BlockUtils.cartesian).mapMulti((face, consumer) -> consumer.accept(block.getRelative(face)));
	}


	/**
	 * Returns a stream containing neighbors of this block that are also part of a network.
	 * 
	 * @param block the block
	 * @return the stream of neighbors
	 */
	default Stream<NetworkComponent<T>> getNeighbors(Block block) {
		return this.getPotentialNeighbors(block).mapMulti((solid, consumer) -> {
			NetworkComponent<T> component = NetworkComponent.getNetworkComponent(solid);
			if(component != null)
				consumer.accept(component);
		});
	}


	/**
	 * Iterates over all blocks the given one is connected to and executes the given action on it. If the action returns true, the iteration will be
	 * interupted and this function returns without iterating over the remaining blocks.
	 * 
	 * @param block  the block
	 * @param action the action
	 * @return if the iteration got interrupted
	 */
	default boolean forEachNeighbor(Block block, Predicate<NetworkComponent<T>> action) {
		return this.getNeighbors(block).anyMatch(action);
	}


	/**
	 * Checks if there are any other networks around the given block that are not accessible by the given owner and returns true if so.
	 * 
	 * @param owner the owner of this component
	 * @param block the block
	 * @return if there are any not owned networks
	 */
	default boolean checkForAdjacentNotOwnedNetworks(UUID owner, Block block) {
		return this.forEachNeighbor(block, component -> {
			Network<?> network = component.getNetwork();
			return network != null && !network.getOwner().equals(owner);
		});
	}

}
