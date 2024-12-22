
package me.gamma.cookies.object.network;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;

import me.gamma.cookies.object.Cable;
import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.collection.Pair;



public class Network<T> {

	final Class<T> clazz;
	private final int id;
	private UUID owner;
	private final TransferRate<T> transferRate;
	private final Location networkCenter;
	private final NetworkMainComponent<T> main;
	private final HashMap<Location, NetworkComponent<T>> components = new HashMap<>();
	private final HashMap<Location, NetworkInterface<T>> interfaces = new HashMap<>();

	/**
	 * Creates a new network.
	 * 
	 * @param clazz         the type class
	 * @param owner         the owner of this network
	 * @param transferRate  the transfer rate of this network
	 * @param networkCenter the location of the main component
	 * @param main          the main component
	 * @param id            the network id
	 */
	Network(Class<T> clazz, UUID owner, TransferRate<T> transferRate, Location networkCenter, NetworkMainComponent<T> main, int id) {
		this.clazz = clazz;
		this.id = id;
		this.owner = owner;
		this.transferRate = transferRate;
		this.networkCenter = networkCenter;
		this.main = main;

		this.update();
	}


	/**
	 * Returns the id of this network.
	 * 
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}


	/**
	 * Returns the owner of this network.
	 * 
	 * @return the owner
	 */
	public UUID getOwner() {
		return this.owner;
	}


	/**
	 * Sets the owner of this network.
	 * 
	 * @param owner the owner
	 */
	public void setOwner(UUID owner) {
		this.owner = owner;
	}


	/**
	 * Checks whether or not the player with the given uuid can access this network.
	 * 
	 * @param uuid the uuid
	 * @return whether the player can access this entwork
	 */
	public boolean canAccess(UUID uuid) {
		return this.main.canAccess(this.getMainBlock(), Bukkit.getOfflinePlayer(uuid));
	}


	/**
	 * Returns the number of components connected to this network.
	 * 
	 * @return the number of components
	 */
	public int getSize() {
		return this.components.size();
	}


	/**
	 * Returns the main component of this network.
	 * 
	 * @return the main component
	 */
	public NetworkMainComponent<T> getMainComponent() {
		return this.main;
	}


	/**
	 * Returns the location of the network center.
	 * 
	 * @return the center
	 */
	public Location getCenter() {
		return this.networkCenter;
	}


	/**
	 * Returns the main tile state block of this network.
	 * 
	 * @return the main block
	 */
	public TileState getMainBlock() {
		return (TileState) this.networkCenter.getBlock().getState();
	}


	/**
	 * Returns a stream of all providers that are part of this network.
	 *
	 * @return the stream of providers
	 */
	public Stream<Provider<T>> streamProviders() {
		return Stream.empty();
	}


	/**
	 * Returns a stream of all providers that are part of this network.
	 *
	 * @return the stream of providers
	 */
	public List<Provider<T>> getProviders() {
		return this.streamProviders().collect(Collectors.toList());
	}


	/**
	 * Stores the given resources in this network and returns what it could not store.
	 *
	 * @param type   the type of the resource
	 * @param amount the amount to be stored
	 * @return the amount that could not be stored
	 */
	public int store(T type, int amount) {
		return Cable.distribute(Cable.TransferMode.ORDERED, type, amount, this.getProviders());
	}


	/**
	 * Fetches resources of the given type from this network that match the given filter and returns it.
	 *
	 * @param type   the type of the resource
	 * @param max    the maximum amount to be fetched
	 * @param filter the filter
	 * @return the pair containing the type of the resource and the fetched amount
	 */
	public Pair<T, Integer> fetch(T type, int max, Filter<T> filter) {
		return Cable.collect(Cable.TransferMode.ORDERED, type, max, filter, this.getProviders());
	}


	/**
	 * Returns a map with the type of the resource as key and the stored amount as value.
	 *
	 * @return the map
	 */
	public HashMap<T, Integer> listResources() {
		return this.streamProviders().filter(p -> !p.isEmpty()).collect(Collectors.toMap(Provider::getType, Provider::amount, Integer::sum, HashMap::new));
	}


	void remove(Location location) {
		this.components.remove(location);
		this.interfaces.remove(location);
	}


	/**
	 * Registers all connected blocks.
	 */
	void update() {
		this.reset();
		this.main.getNeighbors(this.networkCenter.getBlock()).forEach(this::update);
	}


	/**
	 * Registers the given block if it is not already and recursively performs this method on all surrounding blocks.
	 * 
	 * @param block the centered block
	 */
	private void update(Pair<TileState, NetworkComponent<T>> pair) {
		TileState block = pair.left;
		NetworkComponent<T> component = pair.right;

		Location location = block.getLocation();

		if(component instanceof NetworkMainComponent<T> main) {
			if(!this.networkCenter.equals(location))
				main.breakComponent(block);

			return;
		}

		if(this.components.put(location, component) != null)
			return;

		component.setNetwork(block, this.id);

		if(component instanceof NetworkInterface<T> icomponent)
			this.interfaces.put(location, icomponent);

		component.getNeighbors(block.getBlock()).forEach(this::update);
	}


	/**
	 * Returns the component at the given location or null if not part of the network. If the location was part of the network, but no more component
	 * present, it get's removed.
	 * 
	 * @param <C>        the type of the {@link NetworkComponent}
	 * @param components the map of components
	 * @param location   the location
	 * @return the block and component or null
	 */
	private <C extends NetworkComponent<T>> Pair<TileState, C> getComponent(Map<Location, C> components, Location location) {
		TileState block = BlockUtils.getTileState(location);
		if(block == null) {
			this.remove(location);
			return null;
		}

		C component = components.get(location);
		if(component == null) {
			this.remove(location);
			return null;
		}

		return new Pair<>(block, component);
	}


	/**
	 * Returns a list of pairs of all components with their corresponding tile state block.
	 * 
	 * @param <C>        the type of the component
	 * @param components the map of components
	 * @return the stream
	 */
	private <C extends NetworkComponent<T>> Stream<Pair<TileState, C>> getComponents(Map<Location, C> components) {
		return new ArrayList<>(components.keySet()).stream().map(l -> this.getComponent(components, l)).filter(Objects::nonNull);
	}


	/**
	 * Tries to transfer resources from input providers to output providers.
	 */
	public void tick() {
		// iterate over all network interfaces sorted by their priority
		Iterator<Pair<TileState, NetworkInterface<T>>> inputIterator = this.getComponents(this.interfaces).sorted(NetworkInterface.createComparator()).iterator();
		loop: while(inputIterator.hasNext()) {
			Pair<TileState, NetworkInterface<T>> inputPair = inputIterator.next();
			if(inputPair == null)
				continue;

			Filter<T> inputFilter = inputPair.right.getInputFiler(inputPair.left);

			// iterate over all network interfaces on the same channel sorted by their priority again, this time for distributing the items
			Iterator<Pair<TileState, NetworkInterface<T>>> outputIterator = this.getComponents(this.interfaces).filter(NetworkInterface.createFilter(inputPair)).sorted(NetworkInterface.createComparator()).iterator();
			while(outputIterator.hasNext()) {
				Pair<TileState, NetworkInterface<T>> outputPair = outputIterator.next();
				if(outputPair == null)
					continue;

				Filter<T> outputFilter = outputPair.right.getOutputFilter(outputPair.left);

				// iterate through all providers
				List<Provider<T>> inputs = inputPair.right.getInputs(inputPair.left);
				for(Provider<T> input : inputs) {
					if(input.isEmpty())
						continue;

					// get the resources
					final T type = input.getType();
					final int amount0 = input.get(this.transferRate.getTransferRate(type), inputFilter);
					if(amount0 <= 0)
						continue;

					int amount = amount0;

					// store empty outputs to fill already filled outputs first
					List<Provider<T>> empty = new ArrayList<>();

					// try to store the resource in one of the outputs
					List<Provider<T>> outputs = outputPair.right.getOutputs(outputPair.left);

					// count total space
					long space = 0;
					for(Provider<T> output : outputs) {
						boolean match = output.match(type);

						if(output.isEmpty()) {
							if(match || output.canChangeType(type)) {
								output.setType(type);
								space += output.space();
								empty.add(output);
							}
						} else if(match) {
							space += output.space();
						}
					}

					// check if enough resources can be transferred
					int transfer = outputFilter.filter(type, Math.min(amount, (int) Math.min(Integer.MAX_VALUE, space)));
					if(transfer <= 0) {
						input.add(type, amount);
						continue;
					}
					amount -= transfer;

					// transfer resources
					for(Provider<T> output : outputs) {
						if(!output.isEmpty() && output.match(type))
							// if the entire output was stored, continue with the next input network interface
							if((transfer = output.set(type, transfer)) <= 0)
								break;
					}

					// store the rest in empty providers
					if(transfer > 0) {
						for(Provider<T> output : empty)
							if((transfer = output.set(type, transfer)) <= 0)
								break;
					}

					// if not a single resource could be distributed, try the next resource
					amount += transfer;
					input.add(type, amount);
					if(amount0 > amount)
						continue loop;
				}
			}
		}
	}


	/**
	 * Get's called when this network got removed.
	 */
	public void destroy() {
		this.reset();
	}


	/**
	 * Resets all components.
	 */
	private void reset() {
		for(Entry<Location, NetworkComponent<T>> entry : this.components.entrySet()) {
			BlockState state = entry.getKey().getBlock().getState();
			if(state instanceof TileState tile)
				entry.getValue().reset(tile);
		}
		this.components.clear();
	}


	@Override
	public String toString() {
		return String.format("#%08X", this.id);
	}


	@Override
	public int hashCode() {
		return this.id;
	}


	@Override
	public boolean equals(Object obj) {
		return obj instanceof Network<?> network && this.id == network.id && this.clazz == network.clazz;
	}

}
