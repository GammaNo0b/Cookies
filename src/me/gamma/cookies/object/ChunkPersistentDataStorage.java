
package me.gamma.cookies.object;


import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
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



public interface ChunkPersistentDataStorage {

	Registry<ChunkPersistentDataStorage> STORAGES = Registries.CHUNK_STORAGES;

	/**
	 * Returns the name of the storage.
	 * 
	 * @return the name
	 */
	String getIdentifier();


	/**
	 * Returns the {@link PersistentDataObject} for this storage from the given chunk.
	 * 
	 * @param chunk the chunk
	 * @return the persistent data object
	 */
	private PersistentDataObject getStorage(Chunk chunk) {
		PersistentDataContainer container = chunk.getPersistentDataContainer();
		return new PersistentDataObject(container.getOrDefault(new NamespacedKey(Cookies.INSTANCE, this.getIdentifier()), PersistentDataType.TAG_CONTAINER, MinecraftPersistentDataHelper.createNewPersistentDataContainer(container)));
	}


	/**
	 * Stores the {@link PersistentDataObject} for this storage in the given chunk.
	 * 
	 * @param chunk  the chunk
	 * @param object the object
	 */
	private void setStorage(Chunk chunk, PersistentDataObject object) {
		chunk.getPersistentDataContainer().set(new NamespacedKey(Cookies.INSTANCE, this.getIdentifier()), PersistentDataType.TAG_CONTAINER, object.getContainer());
	}


	/**
	 * Loads the storage of the given chunk.
	 * 
	 * @param chunk the chunk
	 */
	private void load(Chunk chunk) {
		this.load(chunk, this.getStorage(chunk));
	}


	/**
	 * Saves the storage of the given chunk.
	 * 
	 * @param chunk the chunk
	 */
	private void save(Chunk chunk) {
		PersistentDataObject object = this.getStorage(chunk);
		this.save(chunk, object);
		this.setStorage(chunk, object);
	}


	/**
	 * Loads the storage of all loaded chunkds.
	 */
	default void loadAll() {
		for(World world : Bukkit.getWorlds())
			for(Chunk chunk : world.getLoadedChunks())
				this.load(chunk);
	}


	/**
	 * Saves the storage of all loaded chunks.
	 */
	default void saveAll() {
		for(World world : Bukkit.getWorlds())
			for(Chunk chunk : world.getLoadedChunks())
				this.save(chunk);
	}


	/**
	 * Loads all chunks.
	 */
	public static void loadAllChunks() {
		STORAGES.forEach(ChunkPersistentDataStorage::loadAll);
	}


	/**
	 * Saves all storages.
	 */
	public static void saveAllChunks() {
		STORAGES.forEachReversed(ChunkPersistentDataStorage::saveAll);
	}


	/**
	 * Loads this persistent data storage in the given chunk.
	 * 
	 * @param chunk  the chunk
	 * @param object the object
	 */
	void load(Chunk chunk, PersistentDataObject object);

	/**
	 * Saves this persistent data storage in the given chunk.
	 * 
	 * @param chunk  the chunk
	 * @param object the object
	 */
	void save(Chunk chunk, PersistentDataObject object);


	/**
	 * Registers this storage.
	 */
	default void register() {
		STORAGES.register(this);
	}


	/**
	 * Returns the listener handling chunk load and unload events.
	 * 
	 * @return the listener
	 */
	static Listener getChunkListener() {
		return new Listener() {

			@EventHandler
			public void onChunkLoad(ChunkLoadEvent event) {
				Chunk chunk = event.getChunk();
				debugChunk(chunk, "chunk loaded");
				STORAGES.forEach(storage -> storage.load(chunk));
			}


			@EventHandler
			public void onChunkUnload(ChunkUnloadEvent event) {
				Chunk chunk = event.getChunk();
				debugChunk(chunk, "chunk unloaded");
				STORAGES.forEachReversed(storage -> storage.save(chunk));
			}


			@EventHandler
			public void onWorldLoad(WorldLoadEvent event) {
				System.out.println("load world " + event.getWorld().getName());
				for(Chunk chunk : event.getWorld().getLoadedChunks()) {
					debugChunk(chunk, "world chunk loaded");
					STORAGES.forEach(storage -> storage.load(chunk));
				}
			}


			@EventHandler
			public void onWorldUnload(WorldUnloadEvent event) {
				System.out.println("unload world " + event.getWorld().getName());
				for(Chunk chunk : event.getWorld().getLoadedChunks()) {
					debugChunk(chunk, "world chunk unloaded");
					STORAGES.forEachReversed(storage -> storage.save(chunk));
				}
			}


			@EventHandler
			public void onWorldSave(WorldSaveEvent event) {
				System.out.println("save world " + event.getWorld().getName());
				for(Chunk chunk : event.getWorld().getLoadedChunks()) {
					debugChunk(chunk, "world chunk saved");
					STORAGES.forEachReversed(storage -> storage.save(chunk));
				}
			}

		};
	}


	static void debugChunk(Chunk chunk, String message) {
		if(chunk.getWorld().getEnvironment() != Environment.NORMAL)
			return;

		if((chunk.getX() != -1 || chunk.getZ() != 0) && (chunk.getX() != -16 || chunk.getZ() != 36))
			return;

		System.out.printf("[Chunk %3d %3d]: %s\n", chunk.getX(), chunk.getZ(), message);
	}

}
