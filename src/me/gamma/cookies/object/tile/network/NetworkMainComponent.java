
package me.gamma.cookies.object.tile.network;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.gamma.cookies.object.block.Ownable;
import me.gamma.cookies.object.block.network.NetworkComponent;
import me.gamma.cookies.object.block.network.NetworkMainComponentBlock;
import me.gamma.cookies.object.network.Network;
import me.gamma.cookies.object.network.NetworkManager;
import me.gamma.cookies.object.network.TransferRate;



public interface NetworkMainComponent<T> extends NetworkComponent<T>, Ownable {

	String KEY_OWNER = "owner";

	/**
	 * Returns the transfer rate the network of this main component should have.
	 * 
	 * @return the transfer rate
	 */
	TransferRate<T> getTransferRate();

	/**
	 * Returns the main component block.
	 * 
	 * @return the main component block
	 */
	NetworkMainComponentBlock<T> getComponentBlock();


	@Override
	default boolean setup(UUID uuid) {
		// check for other main components and their accessibility for the player with the given uuid
		Set<NetworkMainComponent<T>> mainComponents = new HashSet<>();
		if(this.getComponentBlock().forEachConnectedBlockRecursively(this.getBlock(), component -> {
			if(component instanceof NetworkMainComponent<T> main) {
				if(!main.canAccess(Bukkit.getOfflinePlayer(uuid)))
					return true;

				mainComponents.add(main);
			}

			return false;
		}))
			return false;

		// destroy old main components
		for(NetworkMainComponent<T> main : mainComponents) {
			main.destroy();
			main.getComponentBlock().breakComponent(this.getBlock());
		}

		// create the new network
		this.createNetwork(uuid).update();

		return true;
	}


	/**
	 * Creates the network for the given block with the given owner.
	 * 
	 * @param owner the owner
	 */
	default Network<T> createNetwork(UUID owner) {
		Network<T> network = NetworkManager.NETWORK_MANAGER.createNetwork(owner, this.getTransferRate(), this);
		int id = network.getId();
		this.setNetworkID(id);
		return network;
	}


	@Override
	default void destroy() {
		NetworkManager.NETWORK_MANAGER.removeNetwork(this.getNetworkID(), this.getComponentBlock().getType());
	}


	@Override
	default boolean canAccess(UUID uuid) {
		return this.canAccess(Bukkit.getOfflinePlayer(uuid));
	}


	@Override
	default void reset() {}


	@Override
	default void setOwner(UUID owner) {
		this.getNetwork().setOwner(owner);
	}


	@Override
	default UUID getOwner() {
		return this.getNetwork().getOwner();
	}

}
