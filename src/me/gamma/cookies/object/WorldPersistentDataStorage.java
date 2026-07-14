
package me.gamma.cookies.object;


import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.init.Registries;
import me.gamma.cookies.init.Registry;
import me.gamma.cookies.util.collection.PersistentDataObject;
import me.gamma.cookies.util.core.MinecraftPersistentDataHelper;



public interface WorldPersistentDataStorage {

	Registry<WorldPersistentDataStorage> STORAGES = Registries.WORLD_STORAGES;

	/**
	 * Returns the name of the storage.
	 * 
	 * @return the name
	 */
	String getIdentifier();


	/**
	 * Returns the {@link PersistentDataObject} for this storage from the given world.
	 * 
	 * @param world the world
	 * @return the persistent data object
	 */
	private PersistentDataObject getStorage(World world) {
		PersistentDataContainer container = world.getPersistentDataContainer();
		return new PersistentDataObject(container.getOrDefault(new NamespacedKey(Cookies.INSTANCE, this.getIdentifier()), PersistentDataType.TAG_CONTAINER, MinecraftPersistentDataHelper.createNewPersistentDataContainer(container)));
	}


	/**
	 * Stores the {@link PersistentDataObject} for this storage in the given world.
	 * 
	 * @param world  the world
	 * @param object the object
	 */
	private void setStorage(World world, PersistentDataObject object) {
		world.getPersistentDataContainer().set(new NamespacedKey(Cookies.INSTANCE, this.getIdentifier()), PersistentDataType.TAG_CONTAINER, object.getContainer());
	}


	/**
	 * Loads the storage of the given world.
	 * 
	 * @param world the world
	 */
	private void load(World world) {
		this.load(world, this.getStorage(world));
	}


	/**
	 * Saves the storage of the given world.
	 * 
	 * @param world the world
	 */
	private void save(World world) {
		PersistentDataObject object = this.getStorage(world);
		this.save(world, object);
		this.setStorage(world, object);
	}


	/**
	 * Loads all worlds.
	 */
	public static void loadAllWorlds() {
		for(World world : Bukkit.getWorlds())
			STORAGES.forEach(storage -> storage.load(world));
	}


	/**
	 * Saves all worlds.
	 */
	public static void saveAllWorlds() {
		for(World world : Bukkit.getWorlds())
			STORAGES.forEach(storage -> storage.save(world));
	}


	/**
	 * Loads this persistent data storage in the given world.
	 * 
	 * @param world  the world
	 * @param object the object
	 */
	void load(World world, PersistentDataObject object);

	/**
	 * Saves this persistent data storage in the given world.
	 * 
	 * @param world  the world
	 * @param object the object
	 */
	void save(World world, PersistentDataObject object);


	/**
	 * Registers this storage.
	 */
	default void register() {
		STORAGES.register(this);
	}


	/**
	 * Returns the listener handling world load and save events.
	 * 
	 * @return the listener
	 */
	static Listener getWorldListener() {
		return new Listener() {

			@EventHandler
			public void onWorldLoad(WorldLoadEvent event) {
				World world = event.getWorld();
				STORAGES.forEach(storage -> storage.load(world));
			}


			@EventHandler
			public void onWorldUnload(WorldUnloadEvent event) {
				World world = event.getWorld();
				STORAGES.forEach(storage -> storage.save(world));
			}


			@EventHandler
			public void onWorldSave(WorldSaveEvent event) {
				World world = event.getWorld();
				STORAGES.forEach(storage -> storage.save(world));
			}

		};
	}

}
