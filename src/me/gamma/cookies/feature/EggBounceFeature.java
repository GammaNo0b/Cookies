
package me.gamma.cookies.feature;


import java.util.Random;

import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;



public class EggBounceFeature extends SimpleCookieListener {

	public EggBounceFeature() {
		super("egg_bounce");
	}


	@EventHandler
	public void onEggCrush(ProjectileHitEvent event) {
		if(!this.isEnabled())
			return;

		this.bounceEgg(event);
	}


	@EventHandler
	public void onChickenSpawn(CreatureSpawnEvent event) {
		if(!this.isEnabled())
			return;

		event.setCancelled(event.getSpawnReason() == SpawnReason.EGG);
	}


	private void bounceEgg(ProjectileHitEvent event) {
		if(event.getEntity() instanceof Egg) {
			Egg egg = (Egg) event.getEntity();
			Block hit = event.getHitBlock();
			if(hit != null && egg.getVelocity().lengthSquared() > 0.1D) {
				Egg spawned = (Egg) egg.getWorld().spawnEntity(egg.getLocation(), EntityType.EGG);
				spawned.setVelocity(egg.getVelocity().add(egg.getVelocity().multiply(absoluteVectorCoords(event.getHitBlockFace().getDirection()).multiply(-1.8D))));
			}
		}
	}


	private void bounceRandomEgg(ProjectileHitEvent event) {
		if(event.getEntity() instanceof Egg) {
			Egg egg = (Egg) event.getEntity();
			Block hit = event.getHitBlock();
			if(hit != null && egg.getVelocity().lengthSquared() > 0.1D) {
				for(int i = 0; i < 1 + new Random().nextInt(4); i++)
					egg.getWorld().spawnEntity(egg.getLocation().add(event.getHitBlockFace().getDirection().multiply(0.5D)), EntityType.EGG).setVelocity(randomizeDirection(new Random(), event.getHitBlockFace().getDirection().multiply(-0.8D * egg.getVelocity().length())));
			}
		}
	}


	public static Vector absoluteVectorCoords(Vector vector) {
		vector.setX(Math.abs(vector.getX()));
		vector.setY(Math.abs(vector.getY()));
		vector.setZ(Math.abs(vector.getZ()));
		return vector;
	}


	public static Vector randomizeDirection(Random r, Vector vector) {
		return vector.add(new Vector(r.nextDouble() - 0.5D, r.nextDouble() - 0.5D, r.nextDouble() - 0.5D)).multiply(-0.45D);
	}

}
