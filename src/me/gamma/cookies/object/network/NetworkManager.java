
package me.gamma.cookies.object.network;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import me.gamma.cookies.init.Config;
import me.gamma.cookies.object.ChunkPersistentDataStorage;
import me.gamma.cookies.object.Ticker;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.TileEntityStorage;
import me.gamma.cookies.object.tile.network.NetworkMainComponent;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.MapUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;
import me.gamma.cookies.util.math.MathHelper;



public class NetworkManager implements ChunkPersistentDataStorage, Ticker {

	private static final String KEY_NETWORKS = "networks";
	private static final String KEY_MAIN_LOCATION = "mainloc";
	private static final String KEY_NETWORK_OWNER = "owner";

	public static final NetworkManager NETWORK_MANAGER = new NetworkManager();

	private final Map<Integer, Network<?>> networks = new HashMap<>();
	private final Map<Chunk, Set<Network<?>>> networksByChunk = new HashMap<>();

	private long delay = 20;

	private NetworkManager() {}


	@Override
	public String getIdentifier() {
		return "network_manager";
	}


	@Override
	public void load(Chunk chunk, PersistentDataObject object) {
		this.delay = Config.CONFIG.getConfig().getLong("networkUpdateDelay", 20);

		List<PersistentDataObject> array = object.getObjectList(KEY_NETWORKS);
		if(array == null || array.isEmpty()) {
			this.networksByChunk.remove(chunk);
			return;
		}

		Set<Network<?>> set = new HashSet<>();

		for(PersistentDataObject o : array) {
			Location loc = PersistentDataUtils.getLocation(o, KEY_MAIN_LOCATION);
			if(loc == null)
				continue;

			UUID owner = PersistentDataUtils.getUUID(o, KEY_NETWORK_OWNER);
			if(owner == null)
				continue;

			AbstractCustomTileEntity<?, ?> tileEntity = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(loc.getBlock());
			if(tileEntity == null || !(tileEntity instanceof NetworkMainComponent mainComponent))
				continue;

			Network<?> network = mainComponent.createNetwork(owner);
			network.update();
			set.add(network);
		}

		this.networksByChunk.put(chunk, set);
	}


	@Override
	public void save(Chunk chunk, PersistentDataObject object) {
		Set<Network<?>> set = this.networksByChunk.get(chunk);
		if(set == null || set.isEmpty()) {
			object.setObjectList(KEY_NETWORKS, List.of());
			return;
		}

		List<PersistentDataObject> array = new ArrayList<>();

		for(Network<?> network : set) {
			PersistentDataObject o = new PersistentDataObject(object.getAdapterContext());
			PersistentDataUtils.setLocation(o, KEY_MAIN_LOCATION, network.getCenter());
			PersistentDataUtils.setUUID(o, KEY_NETWORK_OWNER, network.getOwner());
			array.add(o);
		}

		object.setObjectList(KEY_NETWORKS, array);
	}


	/**
	 * Creates, registers and returns a new network for the given {@link NetworkMainComponent} at the given location.
	 * 
	 * @param <T>          the type
	 * @param owner        the owner
	 * @param transferRate the transfer rate
	 * @param component    the main component
	 * @return the created network
	 */
	public <T> Network<T> createNetwork(UUID owner, TransferRate<T> transferRate, NetworkMainComponent<T> component) {
		Network<T> network = new Network<>(component.getComponentBlock().getType(), owner, transferRate, component, MathHelper.generateUniqueId(this.networks::containsKey));
		this.networks.put(network.getId(), network);
		MapUtils.getOrStoreDefault(this.networksByChunk, component.getBlock().getChunk(), (Supplier<Set<Network<?>>>) HashSet::new).add(network);
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
	public <T> Network<T> getNetwork(int id, Class<T> clazz) {
		Network<?> network = this.networks.get(id);
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
	public <T> Network<T> removeNetwork(Integer id, Class<T> clazz) {
		Network<?> network = this.networks.remove(id);
		if(network == null)
			return null;

		if(!network.clazz.equals(clazz))
			return null;

		Chunk chunk = network.getCenter().getChunk();
		Set<Network<?>> set = this.networksByChunk.get(chunk);
		if(set != null) {
			set.remove(network);
			if(set.isEmpty())
				this.networksByChunk.remove(chunk);
		}

		network.destroy();
		return (Network<T>) network;
	}


	@Override
	public long getDelay() {
		return this.delay;
	}


	/**
	 * Calls the {@link Network#tick()} method for all networks.
	 */
	@Override
	public void tick() {
		for(World world : Bukkit.getWorlds()) {
			for(Chunk chunk : world.getLoadedChunks()) {
				Set<Network<?>> set = this.networksByChunk.get(chunk);
				if(set != null)
					set.forEach(Network::tick);
			}
		}
	}

}
