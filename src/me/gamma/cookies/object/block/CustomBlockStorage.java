
package me.gamma.cookies.object.block;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.ChunkPersistentDataStorage;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.MapUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class CustomBlockStorage implements ChunkPersistentDataStorage {

	private static final String TAG_BLOCKS = "blocks";
	private static final String TAG_POS = "pos";
	private static final String TAG_TYPE = "type";

	public static final CustomBlockStorage BLOCK_STORAGE = new CustomBlockStorage();

	private final Map<Chunk, Map<Location, AbstractCustomBlock>> blocks = new HashMap<>();

	private CustomBlockStorage() {}


	@Override
	public String getIdentifier() {
		return "blocks";
	}


	@Override
	public void load(Chunk chunk, PersistentDataObject object) {
		List<PersistentDataObject> array = object.getObjectList(TAG_BLOCKS);
		if(array == null || array.isEmpty()) {
			this.blocks.put(chunk, new HashMap<>());
			return;
		}

		Map<Location, AbstractCustomBlock> map = new HashMap<>();
		for(PersistentDataObject o : array) {
			Vector v = PersistentDataUtils.getVector(o, TAG_POS);
			if(v == null)
				continue;

			Location l = v.toLocation(chunk.getWorld());

			String stype = o.getString(TAG_TYPE);
			AbstractCustomBlock block = Blocks.getCustomBlockFromIdentifier(stype);
			if(block == null)
				continue;

			if(!block.checkMaterial(l.getBlock()))
				continue;

			map.put(l, block);
		}

		this.blocks.put(chunk, map);
	}


	@Override
	public void save(Chunk chunk, PersistentDataObject object) {
		Map<Location, AbstractCustomBlock> map = this.blocks.get(chunk);
		if(map == null || map.isEmpty()) {
			object.setObjectList(TAG_BLOCKS, new ArrayList<>());
			return;
		}

		List<PersistentDataObject> array = new ArrayList<>(map.size());

		for(Map.Entry<Location, AbstractCustomBlock> entry : map.entrySet()) {
			Location l = entry.getKey();
			AbstractCustomBlock block = entry.getValue();

			PersistentDataObject o = new PersistentDataObject(object.getAdapterContext());
			PersistentDataUtils.setVector(o, TAG_POS, l.toVector());
			o.setString(TAG_TYPE, block.getIdentifier());

			array.add(o);
		}

		object.setObjectList(TAG_BLOCKS, array);
	}


	/**
	 * Checks if the given block is a custom block.
	 * 
	 * @param block the block location
	 * @return if the block is custom
	 */
	public boolean isCustomBlock(Block block) {
		var map = this.blocks.get(block.getChunk());
		if(map == null)
			return false;

		return map.containsKey(block.getLocation());
	}


	/**
	 * Returns the number of custom blocks in the given chunk.
	 * 
	 * @param chunk the chunk
	 * @return the number of custom blocks
	 */
	public int getCustomBlocksInChunk(Chunk chunk) {
		var map = this.blocks.get(chunk);
		return map == null ? 0 : map.size();
	}


	/**
	 * Returns the custom block at the given block if any.
	 * 
	 * @param block the block location
	 * @return the custom block
	 */
	public AbstractCustomBlock getCustomBlock(Block block) {
		var map = this.blocks.get(block.getChunk());
		if(map == null)
			return null;

		return map.get(block.getLocation());
	}


	/**
	 * Registeres the given custom block at the given block in this storage.
	 * 
	 * @param customBlock the custom block
	 * @param block       the block location
	 */
	public void addCustomBlock(AbstractCustomBlock customBlock, Block block) {
		MapUtils.getOrStoreDefault(this.blocks, block.getChunk(), (Supplier<Map<Location, AbstractCustomBlock>>) HashMap::new).put(block.getLocation(), customBlock);
	}


	/**
	 * Removes the custom block at the given block.
	 * 
	 * @param block the block
	 * @return the removed custom block
	 */
	public AbstractCustomBlock removeCustomBlock(Block block) {
		Map<Location, AbstractCustomBlock> map = this.blocks.get(block.getChunk());
		if(map == null)
			return null;

		AbstractCustomBlock customBlock = map.remove(block.getLocation());

		if(map.isEmpty())
			this.blocks.remove(block.getChunk());

		return customBlock;
	}

}
