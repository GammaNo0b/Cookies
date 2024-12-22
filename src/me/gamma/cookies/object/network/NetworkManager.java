
package me.gamma.cookies.object.network;


import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.object.Ticker;
import me.gamma.cookies.util.math.MathHelper;



public class NetworkManager {

	private static final HashMap<Integer, Network<?>> networks = new HashMap<>();

	/**
	 * Creates, registers and returns a new network for the given {@link NetworkMainComponent} at the given location.
	 * 
	 * @param <T>          the type
	 * @param owner        the owner
	 * @param transferRate the transfer rate
	 * @param component    the main component
	 * @param center       the main components location
	 * @return the created network
	 */
	public static <T> Network<T> createNetwork(UUID owner, TransferRate<T> transferRate, NetworkMainComponent<T> component, Location center) {
		Network<T> network = new Network<>(component.getType(), owner, transferRate, center, component, MathHelper.generateUniqueId(networks::containsKey));
		networks.put(network.getId(), network);
		return network;
	}


	/**
	 * Returns the network with the given id of the given type.
	 * 
	 * @param <T>   the type
	 * @param id    the id of the network
	 * @param clazz the type class of the network
	 * @return the found network or null
	 */
	@SuppressWarnings("unchecked")
	public static <T> Network<T> getNetwork(int id, Class<T> clazz) {
		Network<?> network = networks.get(id);
		if(network == null)
			return null;

		if(!clazz.equals(network.clazz))
			return null;

		return (Network<T>) network;
	}


	/**
	 * Removes the network with the given id.
	 * 
	 * @param <T>   the type
	 * @param id    the id of the network
	 * @param clazz the class type
	 * @return the removed network or null
	 */
	@SuppressWarnings("unchecked")
	public static <T> Network<T> removeNetwork(int id, Class<T> clazz) {
		Network<?> network = networks.remove(id);
		if(network == null)
			return null;

		if(!network.clazz.equals(clazz))
			return null;

		network.destroy();
		return (Network<T>) network;
	}


	/**
	 * Calls the {@link Network#tick()} method for all networks.
	 */
	public static void tick() {
		networks.values().forEach(Network::tick);
	}


	public static void registerTicker() {
		final long delay = Config.CONFIG.getConfig().getLong("networkUpdateDelay", 20);
		Ticker.TICKERS.register(new Ticker() {

			@Override
			public void tick() {
				NetworkManager.tick();
			}


			@Override
			public long getDelay() {
				return delay;
			}

		});
	}

}
