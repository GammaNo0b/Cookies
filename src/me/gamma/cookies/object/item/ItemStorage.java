
package me.gamma.cookies.object.item;


import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.TileEntityStorage;
import me.gamma.cookies.util.collection.PersistentDataObject;



/**
 * Provides item storage that can consume as well as supply items.
 * 
 * @author gamma
 *
 */
public interface ItemStorage extends ItemConsumer, ItemSupplier {

	@Override
	default boolean load(Chunk chunk, PersistentDataObject data) {
		return ItemConsumer.super.load(chunk, data) & ItemSupplier.super.load(chunk, data);
	}


	@Override
	default boolean save(Chunk chunk, PersistentDataObject data) {
		return ItemConsumer.super.save(chunk, data) & ItemSupplier.super.save(chunk, data);
	}


	/**
	 * Returns the list of {@link ItemProvider} of this item storage that act as inputs and as outputs at the same time.
	 * 
	 * @return the list of item providers
	 */
	List<Provider<ItemStack>> getItemProviders();


	@Override
	default List<Provider<ItemStack>> getItemInputs() {
		return this.getItemProviders();
	}


	@Override
	default List<Provider<ItemStack>> getItemOutputs() {
		return this.getItemProviders();
	}


	public static ItemStorage fromVanillaStorage(Block block) {
		if(!(block.getState() instanceof final BlockInventoryHolder inventoryHolder))
			return null;

		return new ItemStorage() {

			@Override
			public Block getBlock() {
				return block;
			}


			@Override
			public List<Provider<ItemStack>> getItemProviders() {
				return ItemProvider.fromInventory(inventoryHolder.getInventory());
			}


			@Override
			public byte getItemInputAccessFlags() {
				return 0x3f;
			}


			@Override
			public void setItemInputAccessFlags(byte flags) {}


			@Override
			public byte getItemOutputAccessFlags() {
				return 0x3f;
			}


			@Override
			public void setItemOutputAccessFlags(byte flags) {}


			@Override
			public boolean canAccessItemInputs(BlockFace face) {
				return true;
			}


			@Override
			public boolean canAccessItemOutputs(BlockFace face) {
				return true;
			}


			@Override
			public boolean isAutoPullingItems() {
				return false;
			}


			@Override
			public boolean isAutoPushingItems() {
				return false;
			}

		};
	}


	/**
	 * Returns the {@link ItemStorage} from the given block.
	 * 
	 * @param block the block
	 * @return the item storage or null
	 */
	public static ItemStorage getItemStorage(Block block) {
		AbstractCustomTileEntity<?, ?> tileEntity = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(block);
		return tileEntity instanceof ItemStorage storage ? storage : fromVanillaStorage(block);
	}

}
