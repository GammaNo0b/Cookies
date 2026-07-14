
package me.gamma.cookies.object.block.network;


import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.gamma.cookies.util.BlockUtils;



public interface NetworkMainComponentBlock<T> extends NetworkComponentBlock<T> {

	/**
	 * Breaks this component.
	 * 
	 * @param block the block
	 */
	void breakComponent(Block block);


	/**
	 * Executes the given action for each block connected to the given one. If one action returns true, the entire process interrupts and returns without
	 * executing any further.
	 * 
	 * @param block  the block
	 * @param action the action to be executed
	 * @return true if the action got interrupted, otherwise false
	 */
	default boolean forEachConnectedBlockRecursively(Block block, Predicate<NetworkComponent<T>> action) {
		NetworkComponent<T> component = NetworkComponent.getNetworkComponent(block);
		if(component == null)
			return false;

		return this.forEachConnectedBlockRecursively(new HashSet<>(), Set.of(component), action);
	}


	/**
	 * Executes the given action for a set of blocks recursively. The <code>visited</code> set contains all blocks that have been already processed. The
	 * <code>generation</code> set contains the blocks to be processed now. If one action returns true, the entire process interrupts and returns without
	 * executing any further.
	 * 
	 * @param visited    the visited blocks
	 * @param generation the next blocks to be processed
	 * @param action     the action to be processed
	 * @return true if the action got interrupted, otherwise false
	 */
	private boolean forEachConnectedBlockRecursively(Set<Block> visited, Set<NetworkComponent<T>> generation, Predicate<NetworkComponent<T>> action) {
		Set<NetworkComponent<T>> next = new HashSet<>();
		for(NetworkComponent<T> entry : generation) {
			Block block = entry.getBlock();
			visited.add(block);
			if(this.getNeighbors(block).filter(component -> !visited.contains(component.getBlock())).anyMatch(component -> {
				if(action.test(component))
					return true;

				next.add(component);
				return false;
			}))
				return true;
		}

		return !next.isEmpty() && this.forEachConnectedBlockRecursively(visited, next, action);
	}


	/**
	 * Checks if there are any other networks around the given location nearby and returns true if so.
	 * 
	 * @param location the location
	 * @return if there are any nearby networks
	 */
	default boolean checkForAdjacentNetworks(Location location) {
		for(BlockFace face : BlockUtils.cartesian) {
			NetworkComponent<T> component = NetworkComponent.getNetworkComponent(location.getBlock().getRelative(face));
			if(component != null && component.getNetwork() != null)
				return true;
		}

		return false;
	}

}
