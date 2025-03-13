
package me.gamma.cookies.object.item;


import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.Supplier;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.block.Cartesian;
import me.gamma.cookies.object.block.machine.MachineConstants;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.property.ByteProperty;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.Pair;



/**
 * Represents a block that can provide items from it.
 * 
 * @author gamma
 *
 */
public interface ItemSupplier extends Cartesian {

	/**
	 * Property to store the item output access flags in a block.
	 */
	ByteProperty ITEM_OUTPUT_ACCESS_FLAGS = new ByteProperty("itemoutputaccessflags");

	/**
	 * Returns the list of {@link ItemProvider} of the given data holder to supply items.
	 * 
	 * @param holder the data holder
	 * @return the list of item providers
	 */
	List<Provider<ItemStack>> getItemOutputs(PersistentDataHolder holder);


	/**
	 * Returns the list of {@link ItemProvider} of the given block on the given block face to supply items.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return the list of item providers
	 */
	default List<Provider<ItemStack>> getItemOutputs(TileState block, BlockFace face) {
		return this.canAccessItemOutputs(block, face) ? this.getItemOutputs(block) : new ArrayList<>();
	}


	/**
	 * Returns the flags for each side of the block to be able to yield items from that side. The first six bits correspond to the six different sides in
	 * {@link BlockUtils#cartesian}. The seventh bit controlls automatic item transfer.
	 * 
	 * @param holder the data holder
	 * @return the access flags
	 */
	default byte getItemOutputAccessFlags(PersistentDataHolder holder) {
		return ITEM_OUTPUT_ACCESS_FLAGS.fetch(holder);
	}


	/**
	 * Checks if the given block can yield items from the given block face.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return if it can accept items
	 */
	default boolean canAccessItemOutputs(TileState block, BlockFace face) {
		return BlockFaceConfigurable.isFaceEnabled(this.getItemOutputAccessFlags(block), block, face);
	}


	/**
	 * Creates the block face config.
	 * 
	 * @return the config
	 */
	default BlockFaceConfig.Config createItemOutputBlockFaceConfig() {
		return new BlockFaceConfig.Config("§6Item Output Configuration", ITEM_OUTPUT_ACCESS_FLAGS, true, MachineConstants.OUTPUT_BORDER_MATERIAL.getType());
	}


	/**
	 * Checks if the given data holder can push items into adjacent item consumer.
	 * 
	 * @param holder the data holder
	 * @return if automation is enabled
	 */
	default boolean isAutoPushingItems(PersistentDataHolder holder) {
		return (this.getItemOutputAccessFlags(holder) & 0x40) != 0;
	}


	/**
	 * Tries to push items to adjacent blocks. Checks first whether automated pushing is enabled.
	 * 
	 * @param block the block
	 * @return whether an item got successfully pushed
	 */
	default boolean tryPushItems(TileState block) {
		if(!this.isAutoPushingItems(block))
			return false;

		for(BlockFace face : BlockUtils.cartesian) {
			if(!this.canAccessItemOutputs(block, face))
				continue;

			Block target = block.getBlock().getRelative(face);
			if(!(target.getState() instanceof TileState tile))
				continue;

			ItemConsumer consumer = ItemConsumer.getItemConsumer(tile);
			if(consumer == null || !consumer.canAccessItemInputs(tile, face.getOppositeFace()))
				continue;

			if(this.removeItem(block, Filter.empty(), stack -> consumer.addStack(tile, stack)))
				return true;
		}

		return false;
	}


	/**
	 * Removes an {@link ItemStack} from the given block.
	 * 
	 * @param block the block
	 * @return the removed item stack
	 */
	default ItemStack removeItem(TileState block) {
		for(BlockFace face : BlockUtils.cartesian) {
			List<Provider<ItemStack>> outputs = this.getItemOutputs(block, face);
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
	 * Removes an {@link ItemStack} of the given {@code type} from the given block.
	 * 
	 * @param block the block
	 * @param type  the type of the item stack
	 * @return the removed item stack
	 */
	default ItemStack removeItem(TileState block, ItemStack type) {
		for(BlockFace face : BlockUtils.cartesian) {
			List<Provider<ItemStack>> outputs = this.getItemOutputs(block, face);
			if(!outputs.isEmpty()) {
				int amount = Supplier.supply(type, type.getMaxStackSize(), this.getItemOutputs(block));
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
	 * Removes an {@link ItemStack} from the given data holder with the filter.
	 * 
	 * @param holder the data holder
	 * @param filter the filter
	 * @return the item stack that got removed
	 */
	default ItemStack removeItem(PersistentDataHolder holder, ItemFilter filter) {
		Pair<ItemStack, Integer> result = Supplier.supply(ItemStack::getMaxStackSize, filter, this.getItemOutputs(holder));
		ItemStack stack = result.left.clone();
		stack.setAmount(result.right);
		return stack;
	}


	/**
	 * Removes an {@link ItemStack} from the given data holder with the filter and passes it to the given consumer. The stack that get's returned by the
	 * consumer will be passed back to the supplier.
	 * 
	 * @param holder   the data holder
	 * @param filter   the filter
	 * @param consumer the consumer
	 * @return if any items got transfered
	 */
	default boolean removeItem(PersistentDataHolder holder, Filter<ItemStack> filter, UnaryOperator<ItemStack> consumer) {
		return removeItem(filter, this.getItemOutputs(holder), consumer);
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
	 * Returns the item supplier of the given block or null if the block has no item supplier.
	 * 
	 * @param holder the block
	 * @return the supplier or null
	 */
	static ItemSupplier getItemSupplier(PersistentDataHolder holder) {
		AbstractCustomBlock custom = Blocks.getCustomBlockFromHolder(holder);
		return custom instanceof ItemSupplier supplier ? supplier : ItemStorage.fromVanillaStorage(holder);
	}

}
