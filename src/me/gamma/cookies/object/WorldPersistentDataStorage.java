
package me.gamma.cookies.object;


import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.init.Registries;
import me.gamma.cookies.init.Registry;
import me.gamma.cookies.util.core.MinecraftPersistentDataHelper;



public interface WorldPersistentDataStorage {

	Registry<WorldPersistentDataStorage> STORAGES = Registries.STORAGES;

	/**
	 * Returns the name of the storage.
	 * 
	 * @return the name
	 */
	String getIdentifier();


	/**
	 * Returns the {@link PersistentDataContainer} for this storage from the given world.
	 * 
	 * @param world the world
	 * @return the persistent data container
	 */
	private PersistentDataContainer getStorage(World world) {
		PersistentDataContainer container = world.getPersistentDataContainer();
		return world.getPersistentDataContainer().getOrDefault(new NamespacedKey(Cookies.INSTANCE, this.getIdentifier()), PersistentDataType.TAG_CONTAINER, MinecraftPersistentDataHelper.createNewPersistentDataContainer(container));
	}


	/**
	 * Stores the {@link PersistentDataContainer} for this storage in the given world.
	 * 
	 * @param world     the world
	 * @param container the container
	 */
	private void setStorage(World world, PersistentDataContainer container) {
		world.getPersistentDataContainer().set(new NamespacedKey(Cookies.INSTANCE, this.getIdentifier()), PersistentDataType.TAG_CONTAINER, container);
	}


	private void load(World world) {
		this.load(world, this.getStorage(world));
	}


	private void save(World world) {
		PersistentDataContainer storage = this.getStorage(world);
		this.save(world, storage);
		this.setStorage(world, storage);
	}


	/**
	 * Loads this persistent data storage in the given world.
	 * 
	 * @param world     the world
	 * @param container the container
	 */
	void load(World world, PersistentDataContainer container);

	/**
	 * Saves this persistent data storage in the given world.
	 * 
	 * @param world     the world
	 * @param container the container
	 */
	void save(World world, PersistentDataContainer container);


	/**
	 * Registers this storage.
	 */
	default void register() {
		STORAGES.register(this);
	}


	/**
	 * Loads all storages for all worlds on this server.
	 */
	static void loadStorages() {
		STORAGES.forEach(storage -> Bukkit.getWorlds().forEach(storage::load));
	}


	/**
	 * Saves all storages for all worlds on this server.
	 */
	static void saveStorages() {
		STORAGES.forEach(storage -> Bukkit.getWorlds().forEach(storage::save));
	}

}
