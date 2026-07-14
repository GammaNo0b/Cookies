
package me.gamma.cookies.object;


import org.bukkit.Chunk;

import me.gamma.cookies.util.collection.PersistentDataObject;



public interface DataStorage {

	/**
	 * Loads this storage from the given data.
	 * @param chunk TODO
	 * @param data the data
	 * 
	 * @return if the loading was successful
	 */
	boolean load(Chunk chunk, PersistentDataObject data);

	/**
	 * Saves this storage to the given data.
	 * @param chunk TODO
	 * @param data the data
	 * 
	 * @return if the saving was successful
	 */
	boolean save(Chunk chunk, PersistentDataObject data);

}
