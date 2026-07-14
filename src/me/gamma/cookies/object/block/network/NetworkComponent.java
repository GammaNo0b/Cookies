
package me.gamma.cookies.object.block.network;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.object.network.Network;
import me.gamma.cookies.object.network.NetworkManager;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.TileEntityStorage;
import me.gamma.cookies.object.tile.network.NetworkMainComponent;



public interface NetworkComponent<T> {

	/**
	 * Returns the block.
	 * 
	 * @return the block
	 */
	Block getBlock();

	/**
	 * Returns the component block.
	 * 
	 * @return the component block
	 */
	NetworkComponentBlock<T> getComponentBlock();

	/**
	 * Sets the network id for this component.
	 * 
	 * @param id the network id
	 */
	void setNetworkID(Integer id);

	/**
	 * Returns the network id of this network or null.
	 * 
	 * @return the network id
	 */
	Integer getNetworkID();


	/**
	 * Sets up the given block for the player with the given uuid.
	 * 
	 * @param uuid the uuid of the player
	 * @return whether or not this block could be set up
	 */
	default boolean setup(UUID uuid) {
		this.setNetworkID(null);

		// check for not accessible neighboring networks
		Set<Network<T>> networks = new HashSet<>();
		if(this.getComponentBlock().getNeighbors(this.getBlock()).anyMatch(component -> {
			Network<T> network = component.getNetwork();
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

		// break main components of different networks
		Iterator<Network<T>> iterator = networks.iterator();
		Network<T> network = iterator.next();
		while(iterator.hasNext()) {
			Network<T> temp = iterator.next();

			NetworkMainComponent<T> main;
			if(network.getSize() >= temp.getSize()) {
				main = temp.getMainComponent();
			} else {
				main = network.getMainComponent();
				network = temp;
			}

			main.destroy();
			main.getComponentBlock().breakComponent(main.getBlock());
		}

		network.update();
		return true;
	}


	/**
	 * Destroys the network component of the given block.
	 * 
	 * @param block the block
	 */
	default void destroy() {
		Network<T> network = this.getNetwork();
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
	default Network<T> getNetwork() {
		Integer id = this.getNetworkID();
		return id == null ? null : NetworkManager.NETWORK_MANAGER.getNetwork(id, this.getComponentBlock().getType());
	}


	/**
	 * Returns whether the player with the given uuid can access this block.
	 * 
	 * @param uuid the uuid of the player
	 * @return if the player has access
	 */
	default boolean canAccess(UUID uuid) {
		Network<T> network = this.getNetwork();
		return network == null || network.canAccess(uuid);
	}


	/**
	 * Resets this network component for this block.
	 */
	default void reset() {
		this.setNetworkID(null);
	}


	/**
	 * Returns the network component at the given block or null.
	 * 
	 * @param <T>   the type of the component
	 * @param block the block
	 * @return the network component
	 */
	@SuppressWarnings("unchecked")
	static <T> NetworkComponent<T> getNetworkComponent(Block block) {
		AbstractCustomTileEntity<?, ?> tileEntity = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(block);
		if(!(tileEntity instanceof NetworkComponent component))
			return null;

		try {
			return (NetworkComponent<T>) component;
		} catch(ClassCastException _) {
			return null;
		}
	}

}
