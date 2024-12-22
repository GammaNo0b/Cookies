
package me.gamma.cookies.util;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;



public class WorldUtils {

	/**
	 * Checks whether the given player can build at the given location.
	 * 
	 * @see org.bukkit.craftbukkit.v1_20_R1.event.CraftEventFactory#canBuild
	 * 
	 * @return if the player can build
	 */
	public static boolean canBuild(World world, HumanEntity player, int x, int z) {
		if(world.getEnvironment() != World.Environment.NORMAL)
			return true;

		int radius = Bukkit.getSpawnRadius();
		if(radius <= 0)
			return true;

		if(Bukkit.getOperators().isEmpty())
			return true;

		if(player.isOp())
			return true;

		Location spawn = world.getSpawnLocation();
		return radius < Math.abs(x - spawn.getBlockX()) && radius < Math.abs(z - spawn.getBlockZ());
	}


	/**
	 * Checks whether the given player can place the given block.
	 * 
	 * @param player the player
	 * @param block  the block
	 * @return if the player can place the block
	 */
	public static boolean canPlace(HumanEntity player, Block block) {
		return canBuild(block.getWorld(), player, block.getX(), block.getZ());
	}


	/**
	 * Checks whether the given player can break the given block.
	 * 
	 * @param player the player
	 * @param block  the block
	 * @return if the player can break the block
	 */
	public static boolean canBreak(HumanEntity player, Block block) {
		return canBuild(block.getWorld(), player, block.getX(), block.getZ());
	}

}
