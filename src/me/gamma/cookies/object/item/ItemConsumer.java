
package me.gamma.cookies.object.item;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.Consumer;
import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.block.Cartesian;
import me.gamma.cookies.object.block.machine.MachineConstants;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.property.ByteProperty;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.ItemUtils;



/**
 * Represents a block that can store items in it.
 * 
 * @author gamma
 *
 */
public interface ItemConsumer extends Cartesian {

	/**
	 * Property to store the item input access flags in a block.
	 */
	ByteProperty ITEM_INPUT_ACCESS_FLAGS = new ByteProperty("iteminputaccessflags");

	/**
	 * Returns the list of {@link ItemProvider} of the given data holder to consume items.
	 * 
	 * @param holder the data holder
	 * @return the list of item providers
	 */
	List<Provider<ItemStack>> getItemInputs(PersistentDataHolder holder);


	/**
	 * Returns the list of {@link ItemProvider} of the given block on the given face to consume items.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return the list of item providers
	 */
	default List<Provider<ItemStack>> getItemInputs(TileState block, BlockFace face) {
		return this.canAccessItemInputs(block, face) ? this.getItemInputs(block) : new ArrayList<>();
	}


	/**
	 * Returns the flags for each side of the block to be able to accept items from that side. The first six bits correspond to the six different sides in
	 * {@link BlockUtils#cartesian}. The seventh bit controlls automatic item transfer.
	 * 
	 * @param holder the data holder
	 * @return the access flags
	 */
	default byte getItemInputAccessFlags(PersistentDataHolder holder) {
		return ITEM_INPUT_ACCESS_FLAGS.fetch(holder);
	}


	/**
	 * Checks if the given block can accept items from the given block face.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return if it can accept items
	 */
	default boolean canAccessItemInputs(TileState block, BlockFace face) {
		return BlockFaceConfigurable.isFaceEnabled(this.getItemInputAccessFlags(block), block, face);
	}


	/**
	 * Creates the block face config.
	 * 
	 * @return the config
	 */
	default BlockFaceConfig.Config createItemInputBlockFaceConfig() {
		return new BlockFaceConfig.Config("§bItem Input Configuration", ITEM_INPUT_ACCESS_FLAGS, true, MachineConstants.INPUT_BORDER_MATERIAL.getType());
	}


	/**
	 * Checks if the given block can pull items from adjacent item providers.
	 * 
	 * @param holder the data holder
	 * @return if automation is enabled
	 */
	default boolean isAutoPullingItems(PersistentDataHolder holder) {
		return (this.getItemInputAccessFlags(holder) & 0x40) != 0;
	}


	/**
	 * Tries to pull items from adjacent blocks. Checks first whether automated pulling is enabled.
	 * 
	 * @param block the block
	 * @return whether an item got successfully pulled
	 */
	default boolean tryPullItems(TileState block) {
		if(!this.isAutoPullingItems(block))
			return false;

		for(BlockFace face : BlockUtils.cartesian) {
			if(!this.canAccessItemInputs(block, face))
				continue;

			Block target = block.getBlock().getRelative(face);
			if(!(target.getState() instanceof TileState tile))
				continue;

			ItemSupplier supplier = ItemSupplier.getItemSupplier(tile);
			if(supplier == null || !supplier.canAccessItemOutputs(tile, face.getOppositeFace()))
				continue;

			if(supplier.removeItem(tile, Filter.empty(), stack -> ItemConsumer.this.addStack(block, stack)))
				return true;
		}

		return false;
	}


	/**
	 * Adds the specified amount of the given stack to the list of {@link ItemProvider}.
	 * 
	 * @param stack  the stack to be consumed
	 * @param amount the amount to be added
	 * @param inputs the list of item provider
	 * @return the stack that couldn't be consumed
	 */
	static ItemStack addStack(ItemStack stack, int amount, List<Provider<ItemStack>> inputs) {
		int rest = Consumer.consume(stack, amount, inputs);
		ItemUtils.increaseItem(stack, rest - amount);
		return stack;
	}


	/**
	 * Adds the specified amount of the given stack to the given data holder.
	 * 
	 * @param holder the data holder
	 * @param stack  the stack to be consumed
	 * @param amount the amount to consume
	 * @return the stack that couldn't be consumed
	 */
	default ItemStack addStack(PersistentDataHolder holder, ItemStack stack, int amount) {
		return addStack(stack, amount, this.getItemInputs(holder));
	}


	/**
	 * Adds the given stack to the given data holder.
	 * 
	 * @param holder the data holder
	 * @param stack  the stack to be consumed
	 * @return the stack that couldn't be consumed
	 */
	default ItemStack addStack(PersistentDataHolder holder, ItemStack stack) {
		return this.addStack(holder, stack, stack.getAmount());
	}


	/**
	 * Adds the given amount of the given stack to the given block on the given facce.
	 * 
	 * @param block  the block
	 * @param face   the block face
	 * @param stack  the stack
	 * @param amount the amount
	 * @return the stack that couldn't be consumed
	 */
	default ItemStack addStack(TileState block, BlockFace face, ItemStack stack, int amount) {
		if(!this.canAccessItemInputs(block, face))
			return stack;

		return this.addStack(block, stack, amount);
	}


	/**
	 * Returns the {@link ItemConsumer} from the given data holder.
	 * 
	 * @param holder the data holder
	 * @return the item consumer or null
	 */
	public static ItemConsumer getItemConsumer(PersistentDataHolder holder) {
		AbstractCustomBlock custom = Blocks.getCustomBlockFromHolder(holder);
		return custom instanceof ItemConsumer consumer ? consumer : ItemStorage.fromVanillaStorage(holder);
	}

}
