
package me.gamma.cookies.object.item;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Consumer;
import me.gamma.cookies.object.DataStorage;
import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.block.machine.MachineConstants;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.TileEntityStorage;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;



/**
 * Represents a block that can store items in it.
 * 
 * @author gamma
 *
 */
public interface ItemConsumer extends DataStorage {

	String KEY_ITEM_INPUT_ACCESS_FLAGS = "iteminputaccessflags";

	@Override
	default boolean load(Chunk chunk, PersistentDataObject data) {
		Byte b = data.getByte(KEY_ITEM_INPUT_ACCESS_FLAGS);
		if(b == null)
			return false;

		this.setItemInputAccessFlags(b);
		return true;
	}


	@Override
	default boolean save(Chunk chunk, PersistentDataObject data) {
		data.setByte(KEY_ITEM_INPUT_ACCESS_FLAGS, this.getItemInputAccessFlags());

		return true;
	}


	/**
	 * Returns the block of this item consumer.
	 * 
	 * @return the block
	 */
	Block getBlock();

	/**
	 * Returns the list of {@link ItemProvider} of this block to consume items.
	 * 
	 * @return the list of item providers
	 */
	List<Provider<ItemStack>> getItemInputs();


	/**
	 * Returns the list of {@link ItemProvider} of this block on the given face to consume items.
	 * 
	 * @param face the block face
	 * @return the list of item providers
	 */
	default List<Provider<ItemStack>> getItemInputs(BlockFace face) {
		return this.canAccessItemInputs(face) ? this.getItemInputs() : new ArrayList<>();
	}


	/**
	 * Returns the flags for each side of this block to be able to accept items from that side. The first six bits correspond to the six different sides
	 * in {@link BlockUtils#cartesian}. The seventh bit controlls automatic item transfer.
	 * 
	 * @return the access flags
	 */
	byte getItemInputAccessFlags();

	/**
	 * Sets the new block face flags.
	 * 
	 * @param flags the flags
	 * @see FluidSupplier#getFluidOutputAccessFlags()
	 */
	void setItemInputAccessFlags(byte flags);


	/**
	 * Checks if the given block can accept items from the given block face.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return if it can accept items
	 */
	default boolean canAccessItemInputs(BlockFace face) {
		return BlockFaceConfigurable.isFaceEnabled(this.getItemInputAccessFlags(), this.getBlock(), face);
	}


	/**
	 * Creates the block face config.
	 * 
	 * @return the config
	 */
	default BlockFaceConfig.Config createItemInputBlockFaceConfig() {
		return new BlockFaceConfig.Config("§bItem Input Configuration", Holder.create(this::getItemInputAccessFlags, this::setItemInputAccessFlags), true, MachineConstants.INPUT_BORDER_MATERIAL.getType());
	}


	/**
	 * Checks if the given block can pull items from adjacent item providers.
	 * 
	 * @param holder the data holder
	 * @return if automation is enabled
	 */
	default boolean isAutoPullingItems() {
		return (this.getItemInputAccessFlags() & 0x40) != 0;
	}


	/**
	 * Tries to pull items from adjacent blocks. Checks first whether automated pulling is enabled.
	 * 
	 * @param block the block
	 * @return whether an item got successfully pulled
	 */
	default boolean tryPullItems() {
		if(!this.isAutoPullingItems())
			return false;

		for(BlockFace face : BlockUtils.cartesian) {
			if(!this.canAccessItemInputs(face))
				continue;

			Block target = this.getBlock().getRelative(face);
			ItemSupplier supplier = ItemSupplier.getItemSupplier(target);
			if(supplier == null || !supplier.canAccessItemOutputs(face.getOppositeFace()))
				continue;

			if(supplier.removeItem(Filter.any(), stack -> ItemConsumer.this.addStack(stack)))
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
	 * Adds the specified amount of the given stack to this block.
	 * 
	 * @param stack  the stack to be consumed
	 * @param amount the amount to consume
	 * @return the stack that couldn't be consumed
	 */
	default ItemStack addStack(ItemStack stack, int amount) {
		return addStack(stack, amount, this.getItemInputs());
	}


	/**
	 * Adds the given stack to the given data holder.
	 * 
	 * @param stack the stack to be consumed
	 * @return the stack that couldn't be consumed
	 */
	default ItemStack addStack(ItemStack stack) {
		return this.addStack(stack, stack.getAmount());
	}


	/**
	 * Adds the given amount of the given stack to this block on the given facce.
	 * 
	 * @param face   the block face
	 * @param stack  the stack
	 * @param amount the amount
	 * @return the stack that couldn't be consumed
	 */
	default ItemStack addStack(BlockFace face, ItemStack stack, int amount) {
		if(!this.canAccessItemInputs(face))
			return stack;

		return this.addStack(stack, amount);
	}


	/**
	 * Returns the {@link ItemConsumer} from the given block.
	 * 
	 * @param block the block
	 * @return the item consumer or null
	 */
	public static ItemConsumer getItemConsumer(Block block) {
		AbstractCustomTileEntity<?, ?> tileEntity = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(block);
		return tileEntity instanceof ItemConsumer consumer ? consumer : ItemStorage.fromVanillaStorage(block);
	}

}
