
package me.gamma.cookies.object.block;


import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.CustomItemData;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.TileEntityStorage;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractCustomTileBlock<B extends AbstractCustomTileBlock<B, T>, T extends AbstractCustomTileEntity<T, B>> extends AbstractCustomBlock {

	/**
	 * Returns a correctly casted version of this custom block.
	 * 
	 * @return this
	 */
	public abstract B castCustomBlock();

	/**
	 * Creates a new tile entity for the given block.
	 * 
	 * @param block the block
	 * @return the created tile entity
	 */
	public abstract T createNewTileEntity(Block block);


	/**
	 * Returns the tile entity associated with the given block.
	 * 
	 * @param block the block
	 * @return the tile entity
	 */
	public T getTileEntity(Block block) {
		return TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(block);
	}


	/**
	 * Transfers data from the tile entity to the item.
	 * 
	 * @param tileData the data of the tile entity
	 * @param itemData the data to be stored in the item
	 */
	protected void transferCustomData(PersistentDataObject tileData, PersistentDataObject itemData) {}


	@Override
	protected void getCustomDropData(Block block, PersistentDataObject data) {
		super.getCustomDropData(block, data);

		T tile = this.getTileEntity(block);
		if(tile == null)
			return;

		PersistentDataObject tileData = new PersistentDataObject(data.getAdapterContext());
		tile.save(null, tileData);
		this.transferCustomData(tileData, data);
	}


	@Override
	public boolean onBlockPlace(Block block, PersistentDataObject data, Player player, BlockPlaceEvent event) {
		if(!super.onBlockPlace(block, data, player, event))
			return false;

		T tileEntity = TileEntityStorage.TILE_ENTITY_STORAGE.createTileEntity(this, block);
		if(!tileEntity.load(null, data)) {
			TileEntityStorage.TILE_ENTITY_STORAGE.removeTileEntity(block);
			return false;
		}

		CustomItemData itemData = AbstractCustomItem.getCustomData(event.getItemInHand());
		if(itemData != null)
			tileEntity.loadFromData(itemData.getData());

		if(!tileEntity.onTileCreated(block, player)) {
			TileEntityStorage.TILE_ENTITY_STORAGE.removeTileEntity(block);
			return false;
		}

		if(tileEntity instanceof Ownable ownable)
			ownable.setOwner(player.getUniqueId());

		return true;
	}


	@Override
	public boolean onBlockBreak(Player player, Block block, BlockBreakEvent event) {
		if(!super.onBlockBreak(player, block, event))
			return false;

		T tileEntity = this.getTileEntity(block);

		if(tileEntity instanceof Ownable ownable && !ownable.canAccess(player))
			return false;

		if(tileEntity != null && !tileEntity.onTileBroken(block, player))
			return false;

		return true;
	}


	@Override
	public void blockBroken(Block block) {
		super.blockBroken(block);

		TileEntityStorage.TILE_ENTITY_STORAGE.removeTileEntity(block);
	}

}
