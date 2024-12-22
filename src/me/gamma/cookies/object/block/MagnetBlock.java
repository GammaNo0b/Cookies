
package me.gamma.cookies.object.block;


import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.block.TileState;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.property.EnumProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.util.Debug;



public class MagnetBlock extends AbstractCustomBlock implements BlockTicker, Switchable {

	public static final EnumProperty<RedstoneMode> REDSTONE_MODE = Properties.REDSTONE_MODE;

	static {
		Debug.setVariable("magnet_range", 10.0D);
		Debug.setVariable("magnet_strength", 1.0D);
	}

	private final Set<Location> locations = new HashSet<>();

	private final String identifier;
	private final Predicate<Entity> attractiveFilter;
	private double range;
	private double strength;

	public MagnetBlock(String identifier, Predicate<Entity> attractiveFilter, double range, double strength) {
		this.identifier = identifier;
		this.attractiveFilter = attractiveFilter;
		this.range = range;
		this.strength = strength;
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public void tick(TileState block) {
		this.range = Debug.getVariable("magnet_range", Double.class, this.range);
		this.strength = Debug.getVariable("magnet_strength", Double.class, this.strength);
		double maxDistSq = this.range * this.range;
		Location center = block.getLocation().add(0.5D, 0.5D, 0.5D);
		for(Entity entity : block.getWorld().getNearbyEntities(center, this.range, this.range, this.range, this.attractiveFilter)) {
			Location pos = entity.getLocation();
			double distSq = center.distance(pos);
			if(distSq > maxDistSq || distSq < 0.25D)
				continue;

			Vector vector = center.toVector().subtract(pos.toVector()).normalize().multiply(this.strength / distSq);
			entity.setVelocity(entity.getVelocity().add(vector));
		}
	}

}
