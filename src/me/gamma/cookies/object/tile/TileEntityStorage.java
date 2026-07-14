
package me.gamma.cookies.object.tile;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.ChunkPersistentDataStorage;
import me.gamma.cookies.object.Ticker;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.object.block.CustomBlockStorage;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.MapUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class TileEntityStorage implements ChunkPersistentDataStorage {

	private static final String TAG_TILE_ENTITIES = "tileentities";
	private static final String TAG_POS = "pos";

	public static final TileEntityStorage TILE_ENTITY_STORAGE = new TileEntityStorage();

	private final Map<Chunk, Map<Location, AbstractCustomTileEntity<?, ?>>> tileEntities = new HashMap<>();
	private final Set<AbstractCustomTileEntity<?, ?>> tickingTileEntities = new HashSet<>();

	private TileEntityStorage() {
		Ticker ticker = new Ticker() {

			@Override
			public void tick() {
				TileEntityStorage.this.tickTileEntities();
			}


			@Override
			public long getDelay() {
				return 1;
			}

		};
		ticker.registerTicker();
	}


	@Override
	public String getIdentifier() {
		return "tileentities";
	}


	@Override
	public void load(Chunk chunk, PersistentDataObject object) {
		List<PersistentDataObject> array = object.getObjectList(TAG_TILE_ENTITIES);
		if(array == null || array.isEmpty()) {
			this.tileEntities.put(chunk, new HashMap<>());
			return;
		}

		Map<Location, AbstractCustomTileEntity<?, ?>> old = this.tileEntities.get(chunk);
		if(old != null)
			this.tickingTileEntities.removeAll(old.values());

		Map<Location, AbstractCustomTileEntity<?, ?>> map = new HashMap<>();
		this.tileEntities.put(chunk, map);
		for(PersistentDataObject o : array) {
			Vector v = PersistentDataUtils.getVector(o, TAG_POS);
			if(v == null)
				continue;

			Location l = v.toLocation(chunk.getWorld());

			AbstractCustomBlock customBlock = CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(l.getBlock());
			if(customBlock == null || !(customBlock instanceof AbstractCustomTileBlock tileBlock))
				continue;

			AbstractCustomTileEntity<?, ?> tile = tileBlock.createNewTileEntity(l.getBlock());
			map.put(l, tile);
			boolean loaded = false;
			try {
				loaded = tile.load(chunk, o);
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				if(!loaded) {
					map.remove(l);
					continue;
				}
			}

			if(tile.isTicking())
				this.tickingTileEntities.add(tile);
		}

		if(map.isEmpty())
			this.tileEntities.remove(chunk);
	}


	@Override
	public void save(Chunk chunk, PersistentDataObject object) {
		Map<Location, AbstractCustomTileEntity<?, ?>> map = this.tileEntities.get(chunk);
		if(map == null || map.isEmpty()) {
			object.setObjectList(TAG_TILE_ENTITIES, new ArrayList<>());
			return;
		}

		List<PersistentDataObject> array = new ArrayList<>(map.size());

		for(Map.Entry<Location, AbstractCustomTileEntity<?, ?>> entry : map.entrySet()) {
			AbstractCustomTileEntity<?, ?> tile = entry.getValue();

			PersistentDataObject o = new PersistentDataObject(object.getAdapterContext());
			if(!tile.save(chunk, o))
				continue;

			PersistentDataUtils.setVector(o, TAG_POS, entry.getKey().toVector());

			array.add(o);
		}

		object.setObjectList(TAG_TILE_ENTITIES, array);
	}


	/**
	 * Returns the tile entity at the given block if any.
	 * 
	 * @param <T>   the type of the tile entity
	 * @param block the block
	 * @return the tile entity
	 */
	@SuppressWarnings("unchecked")
	public <B extends AbstractCustomTileBlock<B, T>, T extends AbstractCustomTileEntity<T, B>> T getTileEntity(Block block) {
		var map = this.tileEntities.get(block.getChunk());
		if(map == null)
			return null;

		AbstractCustomTileEntity<?, ?> tile = map.get(block.getLocation());
		if(tile == null)
			return null;

		try {
			return (T) tile;
		} catch(ClassCastException _) {
			return null;
		}
	}


	/**
	 * Creates a new tile entity of the given tile block at the given block.
	 * 
	 * @param tileBlock the tile block
	 * @param block     the block
	 * @return the created tile entity
	 */
	public <B extends AbstractCustomTileBlock<B, T>, T extends AbstractCustomTileEntity<T, B>> T createTileEntity(AbstractCustomTileBlock<B, T> tileBlock, Block block) {
		T tileEntity = tileBlock.createNewTileEntity(block);
		Map<Location, AbstractCustomTileEntity<?, ?>> map = MapUtils.getOrStoreDefault(this.tileEntities, block.getChunk(), (Supplier<Map<Location, AbstractCustomTileEntity<?, ?>>>) HashMap::new);
		map.put(block.getLocation(), tileEntity);
		if(tileEntity.isTicking())
			this.tickingTileEntities.add(tileEntity);
		return tileEntity;
	}


	/**
	 * Removes the tile entity at the given block.
	 * 
	 * @param block the block
	 * @return the removed tile entity
	 */
	public AbstractCustomTileEntity<?, ?> removeTileEntity(Block block) {
		Map<Location, AbstractCustomTileEntity<?, ?>> map = this.tileEntities.get(block.getChunk());
		if(map == null)
			return null;

		AbstractCustomTileEntity<?, ?> tileEntity = map.remove(block.getLocation());

		if(map.isEmpty())
			this.tileEntities.remove(block.getChunk());

		if(tileEntity != null && tileEntity.isTicking())
			this.tickingTileEntities.remove(tileEntity);

		return tileEntity;
	}


	public void tickTileEntities() {
		Set<AbstractCustomTileEntity<?, ?>> copy = new HashSet<>(this.tickingTileEntities);
		for(AbstractCustomTileEntity<?, ?> tile : copy)
			if(tile.getBlock().getChunk().isLoaded())
				tile.tick();
	}

}
