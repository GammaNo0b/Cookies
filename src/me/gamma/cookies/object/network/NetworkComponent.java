
package me.gamma.cookies.object.network;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.Pair;



public interface NetworkComponent<T> {

	public static final IntegerProperty NETWORK_ID = new IntegerProperty("networkid");

	/**
	 * Returns the type class.
	 * 
	 * @return the type class
	 */
	Class<T> getType();


	/**
	 * Sets up the given block for the player with the given uuid.
	 * 
	 * @param block the block
	 * @param uuid  the uuid of the player
	 * @return whether or not this block could be set up
	 */
	default boolean setup(TileState block, UUID uuid) {
		NETWORK_ID.storeEmpty(block);

		Set<Network<T>> networks = new HashSet<>();
		if(this.getNeighbors(block.getBlock()).anyMatch(pair -> {
			Network<T> network = pair.right.getNetwork(pair.left);
			if(network != null) {
				if(!network.canAccess(uuid))
					return true;

				networks.add(network);
			}

			return false;
		}))
			return false;

		if(networks.isEmpty())
			return true;

		Iterator<Network<T>> iterator = networks.iterator();
		Network<T> network = iterator.next();
		while(iterator.hasNext()) {
			Network<T> temp = iterator.next();

			NetworkMainComponent<T> main;
			TileState tile;
			if(network.getSize() >= temp.getSize()) {
				main = temp.getMainComponent();
				tile = temp.getMainBlock();
			} else {
				main = network.getMainComponent();
				tile = network.getMainBlock();
				network = temp;
			}

			main.destroy(tile);
			main.breakComponent(tile);
		}

		network.update();
		return true;
	}


	/**
	 * Destroys the network component of the given block.
	 * 
	 * @param block the block
	 */
	default void destroy(TileState block) {
		Network<T> network = this.getNetwork(block);
		if(network == null)
			return;

		Bukkit.getScheduler().runTaskLater(Cookies.INSTANCE, network::update, 1);
	}


	/**
	 * Returns the network of this network component of the given block.
	 * 
	 * @param block the block
	 * @return the network
	 */
	default Network<T> getNetwork(TileState block) {
		Integer id = NETWORK_ID.fetch(block);
		return id == null ? null : NetworkManager.getNetwork(id, this.getType());
	}


	/**
	 * Sets the new network id of this network component for the given block.
	 * 
	 * @param block the block
	 * @param id    the network id
	 */
	default void setNetwork(TileState block, int id) {
		NETWORK_ID.store(block, id);
		block.update();
	}


	/**
	 * Resets this network component for the given block.
	 * 
	 * @param block the block
	 */
	default void reset(TileState block) {
		NETWORK_ID.storeEmpty(block);
		block.update();
	}


	/**
	 * Returns a stream containing all the neighbors of the given block.
	 * 
	 * @param block the block
	 * @return the stream of neighbors
	 */
	default Stream<Block> getPotentialNeighbors(Block block) {
		return Stream.of(BlockUtils.cartesian).mapMulti((face, consumer) -> {
			for(int i = 1; i <= 5; i++)
				consumer.accept(block.getRelative(face, i));
		});
	}


	/**
	 * Returns a stream containing neighbors of this block that are also part of a network.
	 * 
	 * @param block the block
	 * @return the stream of neighbors
	 */
	@SuppressWarnings("unchecked")
	default Stream<Pair<TileState, NetworkComponent<T>>> getNeighbors(Block block) {
		return this.getPotentialNeighbors(block).mapMulti((solid, consumer) -> {
			if(solid.getState() instanceof TileState state)
				if(Blocks.getCustomBlockFromBlock(state) instanceof NetworkComponent<?> component)
					if(this.getType().equals(component.getType()))
						consumer.accept(new Pair<>(state, (NetworkComponent<T>) component));
		});
	}


	/**
	 * Iterates over all blocks the given one is connected to and executes the given action on it. If the action returns true, the iteration will be
	 * interupted and this function returns without iterating over the remaining blocks.
	 * 
	 * @param block  the block
	 * @param action the action
	 */
	default void forEachConnectedBlock(Block block, BiFunction<TileState, NetworkComponent<T>, Boolean> action) {
		this.getNeighbors(block).anyMatch(pair -> action.apply(pair.left, pair.right));
	}


	/**
	 * Checks if there are any other networks around the given location that are not accessible by the given owner and returns true if so.
	 * 
	 * @param owner    the owner of this component
	 * @param location the location
	 * @return if there are any not owned networks
	 */
	default boolean checkForAdjacentNotOwnedNetworks(UUID owner, Location location) {
		Holder<Boolean> holder = new Holder<>(false);

		this.forEachConnectedBlock(location.getBlock(), (state, component) -> {
			Network<?> network = component.getNetwork(state);
			if(network != null && !network.getOwner().equals(owner)) {
				holder.value = true;
				return true;
			}

			return false;
		});

		return holder.value;
	}

}
