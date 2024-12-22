
package me.gamma.cookies.object.network;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.block.Ownable;
import me.gamma.cookies.object.block.TileBlockRegister;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public interface NetworkMainComponent<T> extends NetworkComponent<T>, Ownable, TileBlockRegister {

	@Override
	default boolean load(TileState block, PersistentDataObject data) {
		this.createNetwork(block, new UUID(data.getLong("owner_most", 0), data.getLong("owner_least", 0)));
		return true;
	}


	@Override
	default boolean save(TileState block, PersistentDataObject data) {
		UUID owner = this.getOwner(block);
		data.setLong("owner_most", owner.getMostSignificantBits());
		data.setLong("owner_least", owner.getLeastSignificantBits());
		this.destroy(block);
		return true;
	}


	@Override
	default boolean setup(TileState block, UUID uuid) {
		// check for other main components and their accessibility for the player with the given uuid
		Map<TileState, NetworkMainComponent<T>> mainComponents = new HashMap<>();
		if(this.forEachConnectedBlockRecursively(block, (state, component) -> {
			if(component instanceof NetworkMainComponent<T> main) {
				if(!main.canAccess(state, Bukkit.getOfflinePlayer(uuid)))
					return true;

				mainComponents.put(state, main);
			}

			return false;
		}))
			return false;

		// destroy old main components
		for(TileState state : mainComponents.keySet()) {
			NetworkMainComponent<T> main = mainComponents.get(state);
			main.destroy(state);
			main.breakComponent(state);
		}

		// create the new network
		this.createNetwork(block, uuid);

		return true;
	}


	/**
	 * Creates the network for the given block with the given owner.
	 * 
	 * @param block the block
	 * @param owner the owner
	 */
	private void createNetwork(TileState block, UUID owner) {
		Network<T> network = NetworkManager.createNetwork(owner, this.getTransferRate(), this, block.getLocation());
		int id = network.getId();
		NETWORK_ID.store(block, id);
		block.update();
	}


	@Override
	default void destroy(TileState block) {
		NetworkManager.removeNetwork(NETWORK_ID.fetch(block), this.getType());
	}


	/**
	 * Returns the transfer rate the network of this main component should have.
	 * 
	 * @return the transfer rate
	 */
	TransferRate<T> getTransferRate();

	/**
	 * Breaks this component at the given block.
	 * 
	 * @param block the block
	 */
	void breakComponent(TileState block);


	@Override
	default void reset(TileState block) {}


	@Override
	default void setOwner(PersistentDataHolder holder, UUID owner) {
		if(holder instanceof TileState block)
			this.getNetwork(block).setOwner(owner);
	}


	@Override
	default UUID getOwner(PersistentDataHolder holder) {
		return holder instanceof TileState block ? this.getNetwork(block).getOwner() : null;
	}


	/**
	 * Executes the given action for each block connected to the given one. If one action returns true, the entire process interrupts and returns without
	 * executing any further.
	 * 
	 * @param block  the block
	 * @param action the action to be executed
	 * @return true if the action got interrupted, otherwise false
	 */
	default boolean forEachConnectedBlockRecursively(TileState block, BiFunction<TileState, NetworkComponent<T>, Boolean> action) {
		return this.forEachConnectedBlockRecursively(new HashSet<>(), Map.of(block, this), action);
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
	private boolean forEachConnectedBlockRecursively(Set<Block> visited, Map<TileState, NetworkComponent<T>> generation, BiFunction<TileState, NetworkComponent<T>, Boolean> action) {
		HashMap<TileState, NetworkComponent<T>> next = new HashMap<>();
		for(Map.Entry<TileState, NetworkComponent<T>> entry : generation.entrySet()) {
			TileState block = entry.getKey();
			Block solid = block.getBlock();
			visited.add(solid);
			if(this.getNeighbors(solid).filter(pair -> !visited.contains(pair.left.getBlock())).anyMatch(pair -> {
				if(action.apply(pair.left, pair.right))
					return true;

				next.put(pair.left, pair.right);
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
		for(BlockFace face : BlockUtils.cartesian)
			if(location.getBlock().getRelative(face).getState() instanceof TileState tile)
				if(Blocks.getCustomBlockFromBlock(tile) instanceof NetworkComponent<?> component)
					if(component.getType().equals(this.getType()))
						if(component.getNetwork(tile) != null)
							return true;

		return false;
	}

}
