
package me.gamma.cookies.object.block;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class ChunkLoader extends AbstractCustomBlock {

	private static Set<Chunk> loadedChunks = new HashSet<>();

	@Override
	public String getBlockTexture() {
		return HeadTextures.CHUNK_LOADER;
	}


	@Override
	public String getIdentifier() {
		return "chunk_loader";
	}


	@Override
	public boolean canPlace(Player player, Block block) {
		if(loadedChunks.contains(block.getChunk())) {
			player.sendMessage("§eThis Chunk already contains a Chunk Loader!");
			return false;
		}

		if(block.getChunk().isForceLoaded()) {
			player.sendMessage("§eThis Chunk already is loaded!");
			return false;
		}

		return super.canPlace(player, block);
	}


	@Override
	public boolean onBlockPlace(Block block, PersistentDataObject data, Player player, BlockPlaceEvent event) {
		if(!super.onBlockPlace(block, data, player, event))
			return false;

		block.getChunk().setForceLoaded(true);
		player.sendMessage("§aChunkloader activated!");

		return true;
	}


	@Override
	public boolean onBlockBreak(Player player, Block block, BlockBreakEvent event) {
		if(!super.onBlockBreak(player, block, event))
			return false;

		Chunk chunk = block.getChunk();
		if(loadedChunks.remove(chunk)) {
			chunk.setForceLoaded(false);
			event.getPlayer().sendMessage("§cChunkloader deactivated!");
		}

		return true;
	}

}
