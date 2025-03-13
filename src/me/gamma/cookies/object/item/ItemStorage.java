
package me.gamma.cookies.object.item;


import java.util.List;

import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.AbstractCustomBlock;



/**
 * Provides item storage that can consume as well as supply items.
 * 
 * @author gamma
 *
 */
public interface ItemStorage extends ItemConsumer, ItemSupplier {

	/**
	 * Returns the list of {@link ItemProvider} of the given block that act as inputs and as outputs at the same time.
	 * 
	 * @param holder the block
	 * @return the list of item providers
	 */
	List<Provider<ItemStack>> getItemProviders(PersistentDataHolder holder);


	@Override
	default List<Provider<ItemStack>> getItemInputs(PersistentDataHolder holder) {
		return this.getItemProviders(holder);
	}


	@Override
	default List<Provider<ItemStack>> getItemOutputs(PersistentDataHolder holder) {
		return this.getItemProviders(holder);
	}


	public static ItemStorage fromVanillaStorage(PersistentDataHolder holder) {
		if(!(holder instanceof final BlockInventoryHolder inventoryHolder))
			return null;

		return new ItemStorage() {

			@Override
			public List<Provider<ItemStack>> getItemProviders(PersistentDataHolder holder) {
				return ItemProvider.fromInventory(inventoryHolder.getInventory());
			}


			@Override
			public byte getItemInputAccessFlags(PersistentDataHolder holder) {
				return 0x3f;
			}


			@Override
			public byte getItemOutputAccessFlags(PersistentDataHolder holder) {
				return 0x3f;
			}


			@Override
			public boolean canAccessItemInputs(TileState block, BlockFace face) {
				return true;
			}


			@Override
			public boolean canAccessItemOutputs(TileState block, BlockFace face) {
				return true;
			}


			@Override
			public boolean isAutoPullingItems(PersistentDataHolder holder) {
				return false;
			}


			@Override
			public boolean isAutoPushingItems(PersistentDataHolder holder) {
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
	public static ItemStorage getItemStorage(TileState block) {
		AbstractCustomBlock custom = Blocks.getCustomBlockFromBlock(block);
		return custom instanceof ItemStorage storage ? storage : ItemStorage.fromVanillaStorage(block);
	}

}
