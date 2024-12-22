
package me.gamma.cookies.object.block;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.object.WorldPersistentDataStorage;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.util.collection.PersistentDataObject;
import me.gamma.cookies.util.core.MinecraftPersistentDataHelper;



/**
 * Represents a block that's locations should be saved.
 * 
 * @author gamma
 *
 */
public interface BlockRegister extends WorldPersistentDataStorage {

	String TAG_BLOCKS = "blocks";

	/**
	 * The set to store the locations in.
	 * 
	 * @return the set
	 */
	Set<Location> getLocations();


	@Override
	default void load(World world, PersistentDataContainer container) {
		Set<Location> locations = this.getLocations();
		List<PersistentDataContainer> containers = container.getOrDefault(new NamespacedKey(Cookies.INSTANCE, TAG_BLOCKS), PersistentDataType.LIST.dataContainers(), new ArrayList<>());
		for(PersistentDataContainer c : containers) {
			Vector pos = Properties.POS.fetch(c);
			if(pos == null)
				continue;

			Location location = pos.toLocation(world);
			try {
				if(!this.load(location, new PersistentDataObject(c)))
					continue;
			} catch(Exception e) {
				System.err.println("Unable to load block register.");
				e.printStackTrace();
				continue;
			}

			locations.add(location);
		}
	}


	/**
	 * Get's executed on each stored location when the server is starting.
	 * 
	 * @param location the current location
	 * @param data     the data of the block
	 * @return returns whether the current location should be registered
	 */
	default boolean load(Location location, PersistentDataObject data) {
		return true;
	}


	@Override
	default void save(World world, PersistentDataContainer container) {
		List<PersistentDataContainer> list = new ArrayList<>();
		for(Location location : this.getLocations()) {
			if(!world.equals(location.getWorld()))
				continue;

			PersistentDataContainer c = MinecraftPersistentDataHelper.createNewPersistentDataContainer(container);
			if(!this.save(location, new PersistentDataObject(c)))
				continue;

			Properties.POS.store(c, location.toVector());
			list.add(c);
		}
		container.set(new NamespacedKey(Cookies.INSTANCE, TAG_BLOCKS), PersistentDataType.LIST.dataContainers(), list);
	}


	/**
	 * Get's executed on each stored location when the server is stopping.
	 * 
	 * @param location the current location
	 * @param data     the data to be stored
	 * @return returns whether the current location should be saved
	 */
	default boolean save(Location location, PersistentDataObject data) {
		return true;
	}

}
