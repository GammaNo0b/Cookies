
package me.gamma.cookies.util.core;


import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BoneMealItem;



/**
 * Utility class for manipulating worlds using the minecraft code and not bukkit code.
 * 
 * @author gamma
 *
 */
public class MinecraftWorldHelper {

	/**
	 * Returns the underlying {@link ServerLevel} of a bukkit {@link World}.
	 * 
	 * @param world the bukkit world
	 * @return the minecraft server level
	 */
	public static ServerLevel toLevel(World world) {
		return ((CraftWorld) world).getHandle();
	}


	/**
	 * Returns the random source of the given world.
	 * 
	 * @param world the world
	 * @return the random source
	 */
	public static RandomSource getRandom(World world) {
		return toLevel(world).getRandom();
	}


	/**
	 * Spawns growth particles at the given block.
	 * 
	 * @param block the block
	 */
	public static void addGrowthParticles(Block block) {
		BoneMealItem.addGrowthParticles(toLevel(block.getWorld()), new BlockPos(block.getX(), block.getY(), block.getZ()), 3);
	}

}
