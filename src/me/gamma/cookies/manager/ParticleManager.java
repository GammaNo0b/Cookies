
package me.gamma.cookies.manager;


import java.util.Random;
import java.util.function.Consumer;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.util.Vector;

import me.gamma.cookies.util.Utils;



public class ParticleManager {

	private static final Random random = new Random();

	public static void drawLine(Location start, Location end, int amount, int r, int g, int b) {
		World world = start.getWorld();
		Vector delta = end.clone().subtract(start).toVector().multiply(1.0D / amount);
		for(Location pos = start.clone(); amount >= 0; pos.add(delta), amount--)
			world.spawnParticle(Particle.DUST, pos.getX(), pos.getY(), pos.getZ(), 1, 0, 0, 0, 1, new DustOptions(Color.fromRGB(r, g, b), 1.0F));
	}


	public static <T> void spawnParticle(Particle particle, T data, double chance, int minCount, int maxCount, Location location, double xOffset, double yOffset, double zOffset) {
		if(chance > random.nextDouble())
			location.getWorld().spawnParticle(particle, location, random.nextInt(minCount, maxCount), zOffset, xOffset, yOffset, data);
	}
	
	public static void drawAnimatedLine(Location start, Location end, long delay, int amount, Consumer<Location> particleGenerator) {
		Vector delta = end.clone().subtract(start).toVector().multiply(1.0D / amount);
		Location pos = start.clone();
		for(int i = 0; i < amount; i++, pos.add(delta)) {
			final Location l = pos.clone();
			Utils.runLater(i * delay, () -> particleGenerator.accept(l));
		}
	}

}
