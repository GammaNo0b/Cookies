
package me.gamma.cookies.object.item;


import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.DataStorage;
import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.Supplier;
import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.block.machine.MachineConstants;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.TileEntityStorage;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.Pair;
import me.gamma.cookies.util.collection.PersistentDataObject;



/**
 * Represents a block that can provide items from it.
 * 
 * @author gamma
 *
 */
public interface ItemSupplier extends DataStorage {

	String KEY_ITEM_OUTPUT_ACCESS_FLAGS = "itemoutputaccessflags";

	@Override
	default boolean load(Chunk chunk, PersistentDataObject data) {
		Byte b = data.getByte(KEY_ITEM_OUTPUT_ACCESS_FLAGS);
		if(b == null)
			return false;

		this.setItemOutputAccessFlags(b);
		return true;
	}


	@Override
	default boolean save(Chunk chunk, PersistentDataObject data) {
		data.setByte(KEY_ITEM_OUTPUT_ACCESS_FLAGS, this.getItemOutputAccessFlags());

		return true;
	}


	/**
	 * Returns the block of this item supplier.
	 * 
	 * @return the block
	 */
	Block getBlock();

	/**
	 * Returns the list of {@link ItemProvider} to supply items.
	 * 
	 * @return the list of item providers
	 */
	List<Provider<ItemStack>> getItemOutputs();


	/**
	 * Returns the list of {@link ItemProvider} of the given block on the given block face to supply items.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return the list of item providers
	 */
	default List<Provider<ItemStack>> getItemOutputs(BlockFace face) {
		return this.canAccessItemOutputs(face) ? this.getItemOutputs() : new ArrayList<>();
	}


	/**
	 * Returns the flags for each side of the block to be able to yield items from that side. The first six bits correspond to the six different sides in
	 * {@link BlockUtils#cartesian}. The seventh bit controlls automatic item transfer.
	 * 
	 * @return the access flags
	 */
	byte getItemOutputAccessFlags();

	/**
	 * Sets the new block face flags.
	 * 
	 * @param flags the flags
	 * @see FluidSupplier#getFluidOutputAccessFlags()
	 */
	void setItemOutputAccessFlags(byte flags);


	/**
	 * Checks if the given block can yield items from the given block face.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return if it can accept items
	 */
	default boolean canAccessItemOutputs(BlockFace face) {
		return BlockFaceConfigurable.isFaceEnabled(this.getItemOutputAccessFlags(), this.getBlock(), face);
	}


	/**
	 * Creates the block face config.
	 * 
	 * @return the config
	 */
	default BlockFaceConfig.Config createItemOutputBlockFaceConfig() {
		return new BlockFaceConfig.Config("§6Item Output Configuration", Holder.create(this::getItemOutputAccessFlags, this::setItemOutputAccessFlags), true, MachineConstants.OUTPUT_BORDER_MATERIAL.getType());
	}


	/**
	 * Checks if this supllier can push items into adjacent item consumer.
	 * 
	 * @return if automation is enabled
	 */
	default boolean isAutoPushingItems() {
		return (this.getItemOutputAccessFlags() & 0x40) != 0;
	}


	/**
	 * Tries to push items to adjacent blocks. Checks first whether automated pushing is enabled.
	 * 
	 * @param block the block
	 * @return whether an item got successfully pushed
	 */
	default boolean tryPushItems() {
		if(!this.isAutoPushingItems())
			return false;

		for(BlockFace face : BlockUtils.cartesian) {
			if(!this.canAccessItemOutputs(face))
				continue;

			Block target = this.getBlock().getRelative(face);
			ItemConsumer consumer = ItemConsumer.getItemConsumer(target);
			if(consumer == null || !consumer.canAccessItemInputs(face.getOppositeFace()))
				continue;

			if(this.removeItem(Filter.any(), stack -> consumer.addStack(stack)))
				return true;
		}

		return false;
	}


	/**
	 * Removes an {@link ItemStack} from this block.
	 * 
	 * @return the removed item stack
	 */
	default ItemStack removeItem() {
		for(BlockFace face : BlockUtils.cartesian) {
			List<Provider<ItemStack>> outputs = this.getItemOutputs(face);
			if(!outputs.isEmpty()) {
				Pair<ItemStack, Integer> result = Supplier.supply(ItemStack::getMaxStackSize, outputs);
				if(ItemUtils.isEmpty(result.left))
					return null;

				ItemStack stack = result.left.clone();
				stack.setAmount(result.right);
				return stack;
			}
		}
		return null;
	}


	/**
	 * Removes an {@link ItemStack} of the given {@code type} from this block.
	 * 
	 * @param type the type of the item stack
	 * @return the removed item stack
	 */
	default ItemStack removeItem(ItemStack type) {
		for(BlockFace face : BlockUtils.cartesian) {
			List<Provider<ItemStack>> outputs = this.getItemOutputs(face);
			if(!outputs.isEmpty()) {
				int amount = Supplier.supply(type, type.getMaxStackSize(), outputs);
				if(amount == 0)
					return null;

				ItemStack stack = type.clone();
				stack.setAmount(amount);
				return stack;
			}
		}
		return null;
	}


	/**
	 * Removes an {@link ItemStack} from this block with the filter.
	 * 
	 * @param filter the filter
	 * @return the item stack that got removed
	 */
	default ItemStack removeItem(ItemFilter filter) {
		Pair<ItemStack, Integer> result = Supplier.supply(ItemStack::getMaxStackSize, filter, this.getItemOutputs());
		ItemStack stack = result.left.clone();
		stack.setAmount(result.right);
		return stack;
	}


	/**
	 * Removes an {@link ItemStack} from this block with the filter and passes it to the given consumer. The stack that get's returned by the consumer
	 * will be passed back to the supplier.
	 * 
	 * @param filter   the filter
	 * @param consumer the consumer
	 * @return if any items got transfered
	 */
	default boolean removeItem(Filter<ItemStack> filter, UnaryOperator<ItemStack> consumer) {
		return removeItem(filter, this.getItemOutputs(), consumer);
	}


	/**
	 * Removes an {@link ItemStack} from the given list of {@link ItemProvider} with the given filter and passes it to the consumer. The stack that get's
	 * returned by the consumer will be passed back to the list of supplier.
	 * 
	 * @param filter   the filter
	 * @param outputs  the list of item provider
	 * @param consumer the consumer
	 * @return if any items got transfered
	 */
	static boolean removeItem(Filter<ItemStack> filter, List<Provider<ItemStack>> outputs, UnaryOperator<ItemStack> consumer) {
		return Supplier.supply(ItemStack::getMaxStackSize, filter, outputs, pair -> {
			ItemStack stack = new ItemStack(pair.left);
			stack.setAmount(pair.right);
			stack = consumer.apply(stack);
			return new Pair<>(stack, ItemUtils.isEmpty(stack) ? 0 : stack.getAmount());
		});
	}


	/**
	 * Returns the {@link ItemSupplier} from the given block.
	 * 
	 * @param block the block
	 * @return the item supplier or null
	 */
	public static ItemSupplier getItemSupplier(Block block) {
		AbstractCustomTileEntity<?, ?> tileEntity = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(block);
		return tileEntity instanceof ItemSupplier supplier ? supplier : ItemStorage.fromVanillaStorage(block);
	}

}