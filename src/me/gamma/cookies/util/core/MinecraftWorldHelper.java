
package me.gamma.cookies.util.core;


import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;

import net.minecraft.util.RandomSource;



/**
 * Utility class for manipulating worlds using the minecraft code and not bukkit code.
 * 
 * @author gamma
 *
 */
public class MinecraftWorldHelper {

	/**
	 * Returns the random source of the given world.
	 * 
	 * @param world the world
	 * @return the random source
	 */
	public static RandomSource getRandom(World world) {
		return ((CraftWorld) world).getHandle().H_();
	}

}
