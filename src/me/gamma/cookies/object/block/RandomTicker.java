
package me.gamma.cookies.object.block;


import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;

import me.gamma.cookies.object.Ticker;
import me.gamma.cookies.util.core.MinecraftWorldHelper;
import net.minecraft.util.RandomSource;



public class RandomTicker implements Ticker {

	public static final RandomTicker RANDOM_TICKER = new RandomTicker();

	private RandomTicker() {}


	private Location getRandomBlockPos(Chunk chunk, int section) {
		RandomSource random = MinecraftWorldHelper.getRandom(chunk.getWorld());
		return new Location(chunk.getWorld(), chunk.getX() * 16 + random.nextInt(16), section + random.nextInt(16), chunk.getZ() * 16 + random.nextInt(16));
	}


	private void tickChunk(Chunk chunk, int randomBlocks) {
		int customBlocks = CustomBlockStorage.BLOCK_STORAGE.getCustomBlocksInChunk(chunk);
		if(customBlocks <= 0)
			return;

		if(customBlocks < randomBlocks)
			randomBlocks = customBlocks;

		for(int y = chunk.getWorld().getMinHeight(); y < chunk.getWorld().getMaxHeight(); y += 16) {
			for(int i = 0; i < randomBlocks; i++) {
				Location l = this.getRandomBlockPos(chunk, y);
				AbstractCustomBlock block = CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(l.getBlock());
				if(block != null)
					block.onRandomTick(l.getBlock());
			}
		}
	}


	private void tickWorld(World world) {
		int randomBlocks = world.getGameRuleValue(GameRule.RANDOM_TICK_SPEED);
		for(Chunk chunk : world.getLoadedChunks())
			this.tickChunk(chunk, randomBlocks);
	}


	@Override
	public void tick() {
		for(World world : Bukkit.getWorlds())
			this.tickWorld(world);
	}


	@Override
	public long getDelay() {
		return 1;
	}

}
