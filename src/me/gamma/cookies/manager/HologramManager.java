
package me.gamma.cookies.manager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.object.WorldPersistentDataStorage;
import me.gamma.cookies.util.core.MinecraftPersistentDataHelper;
import me.gamma.cookies.util.math.IDGen;



public class HologramManager implements WorldPersistentDataStorage {

	public static final String TAG_HOLOGRAMS = "holograms";

	private final IDGen<Integer> idgen = IDGen.newIntIDGen();

	private final HashMap<Integer, Hologram> holograms = new HashMap<>();

	public HologramManager() {
		this.register();
	}


	/**
	 * Returns the hologram with the given id or null.
	 * 
	 * @param id the id
	 * @return the hologram
	 */
	Hologram getHologram(int id) {
		return this.holograms.get(id);
	}


	/**
	 * Creates a hologram displaying the given lines at the given location.
	 * 
	 * @param location the location
	 * @param lines    the lines to be displayed
	 * @return the id of the created hologram
	 */
	public int createHologram(Location location, String... lines) {
		int id = this.idgen.generate();
		Hologram hologram = new Hologram(id, location);
		this.holograms.put(id, hologram);
		hologram.setText(lines);
		return id;
	}


	/**
	 * Sets new lines for the hologram with the given id.
	 * 
	 * @param id    the id
	 * @param lines the new lines
	 * @return whether a hologram was found and the text got replaced
	 */
	public boolean editHologram(int id, String... lines) {
		Hologram hologram = this.getHologram(id);
		if(hologram == null)
			return false;

		hologram.setText(lines);
		return true;
	}


	/**
	 * Removes the hologram with the given id. Returns true if the hologram was found, otherwise false.
	 * 
	 * @param id the id
	 * @return whether the hologram was found and removed
	 */
	public boolean removeHologram(int id) {
		Hologram hologram = this.getHologram(id);
		if(hologram == null)
			return false;

		hologram.remove();
		this.holograms.remove(id);
		this.idgen.release(id);
		return true;
	}


	@Override
	public String getIdentifier() {
		return "holograms";
	}


	@Override
	public void load(World world, PersistentDataContainer container) {
		this.idgen.reset();
		List<PersistentDataContainer> containers = container.getOrDefault(new NamespacedKey(Cookies.INSTANCE, TAG_HOLOGRAMS), PersistentDataType.LIST.dataContainers(), new ArrayList<>());
		for(PersistentDataContainer c : containers) {
			int id = this.idgen.generate();
			Hologram hologram = Hologram.load(id, world, c);
			if(hologram != null)
				this.holograms.put(id, hologram);
			else
				this.idgen.release(id);
		}
	}


	@Override
	public void save(World world, PersistentDataContainer container) {
		List<PersistentDataContainer> containers = new ArrayList<>(this.holograms.size());
		for(Hologram hologram : this.holograms.values()) {
			if(!hologram.isIn(world))
				continue;

			PersistentDataContainer c = MinecraftPersistentDataHelper.createNewPersistentDataContainer(container);
			hologram.save(c);
			containers.add(c);
		}
		container.set(new NamespacedKey(Cookies.INSTANCE, TAG_HOLOGRAMS), PersistentDataType.LIST.dataContainers(), containers);
	}

}
