
package me.gamma.cookies.object.item;


import java.util.List;

import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;

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
	 * @param block the block
	 * @return the list of item providers
	 */
	List<Provider<ItemStack>> getItemProviders(TileState block);


	@Override
	default List<Provider<ItemStack>> getItemInputs(TileState block) {
		return this.getItemProviders(block);
	}


	@Override
	default List<Provider<ItemStack>> getItemOutputs(TileState block) {
		return this.getItemProviders(block);
	}


	public static ItemStorage fromVanillaStorage(TileState block) {
		if(!(block instanceof final BlockInventoryHolder holder))
			return null;

		return new ItemStorage() {

			@Override
			public List<Provider<ItemStack>> getItemProviders(TileState block) {
				return ItemProvider.fromInventory(holder.getInventory());
			}


			@Override
			public byte getItemInputAccessFlags(TileState block) {
				return 0x3f;
			}


			@Override
			public byte getItemOutputAccessFlags(TileState block) {
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
			public boolean isAutoPullingItems(TileState block) {
				return false;
			}


			@Override
			public boolean isAutoPushingItems(TileState block) {
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
