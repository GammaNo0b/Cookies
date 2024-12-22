
package me.gamma.cookies.manager;


import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.UUIDArrayProperty;
import me.gamma.cookies.object.property.VectorProperty;



public class Hologram {

	private static final VectorProperty HOLOGRAM_POS = Properties.POS;
	private static final UUIDArrayProperty HOLOGRAM_IDS = new UUIDArrayProperty("ids");

	private final int id;
	private final Location location;
	private UUID[] uuids = {};

	Hologram(int id, Location location) {
		this.id = id;
		this.location = location;
	}


	boolean isIn(World world) {
		return world.equals(this.location.getWorld());
	}


	void setText(String[] lines) {
		this.remove();

		int len = lines.length;
		this.uuids = new UUID[len];
		Location location = this.location.clone();
		World world = location.getWorld();
		for(int i = 0; i < len; i++) {
			ArmorStand armorstand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
			armorstand.setCustomName(lines[len - i - 1]);
			armorstand.setCustomNameVisible(true);
			armorstand.setCollidable(false);
			armorstand.setGravity(false);
			armorstand.setInvisible(true);
			armorstand.setInvulnerable(true);
			armorstand.setMarker(true);

			this.uuids[i] = armorstand.getUniqueId();
			location.add(0.0D, 0.25D, 0.0D);
		}
	}


	void remove() {
		for(UUID uuid : this.uuids)
			if(Bukkit.getEntity(uuid) instanceof ArmorStand armorstand)
				armorstand.remove();
	}


	@Override
	public int hashCode() {
		return this.id;
	}


	@Override
	public boolean equals(Object obj) {
		return obj instanceof Hologram hologram && this.id == hologram.id;
	}


	void save(PersistentDataContainer container) {
		HOLOGRAM_POS.store(container, this.location.toVector());
		HOLOGRAM_IDS.store(container, this.uuids);
	}


	static Hologram load(int id, World world, PersistentDataContainer container) {
		Vector pos = HOLOGRAM_POS.fetch(container);
		if(pos == null)
			return null;

		Location location = pos.toLocation(world);
		Hologram hologram = new Hologram(id, location);
		hologram.uuids = HOLOGRAM_IDS.fetch(container);
		if(hologram.uuids == null)
			return null;

		return hologram;
	}

}
