
package me.gamma.cookies.object.fluid;


import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.gamma.cookies.object.DataStorage;
import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.Supplier;
import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.TileEntityStorage;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.Pair;
import me.gamma.cookies.util.collection.PersistentDataObject;



/**
 * Represents a block that can provide fluids from it.
 * 
 * @author gamma
 *
 */
public interface FluidSupplier extends DataStorage {

	String KEY_FLUID_OUTPUT_ACCESS_FLAGS = "fluidoutputaccessflags";

	@Override
	default boolean load(Chunk chunk, PersistentDataObject data) {
		Byte b = data.getByte(KEY_FLUID_OUTPUT_ACCESS_FLAGS);
		if(b == null)
			return false;

		this.setFluidOutputAccessFlags(b);
		return true;
	}


	@Override
	default boolean save(Chunk chunk, PersistentDataObject data) {
		data.setByte(KEY_FLUID_OUTPUT_ACCESS_FLAGS, this.getFluidOutputAccessFlags());

		return true;
	}


	/**
	 * Returns the block of this fluid supplier.
	 * 
	 * @return the block
	 */
	Block getBlock();

	/**
	 * Returns the list of {@link FluidProvider} of the given data holder to supply fluids.
	 * 
	 * @param holder the data holder
	 * @return the list of fluid providers
	 */
	List<FluidProvider> getFluidOutputs();


	/**
	 * Returns the list of {@link FluidProvider} of the given block on the given face to supply fluids.
	 * 
	 * @param face the block face
	 * @return the list of fluid providers
	 */
	default List<FluidProvider> getFluidOutputs(BlockFace face) {
		return this.canAccessFluidOutputs(face) ? this.getFluidOutputs() : new ArrayList<>();
	}


	/**
	 * Returns the flags for each side of the block to be able to yield fluids from that side. The first six bits correspond to the six different sides in
	 * {@link BlockUtils#cartesian}. The seventh bit controlls automatic fluid transfer.
	 * 
	 * @return the access flags
	 */
	byte getFluidOutputAccessFlags();

	/**
	 * Sets the new block face flags.
	 * 
	 * @param flags the flags
	 * @see FluidSupplier#getFluidOutputAccessFlags()
	 */
	void setFluidOutputAccessFlags(byte flags);


	/**
	 * Checks if the given block can yield fluids from the given block face.
	 * 
	 * @param face the block face
	 * @return if it can accept fluids
	 */
	default boolean canAccessFluidOutputs(BlockFace face) {
		return BlockFaceConfigurable.isFaceEnabled(this.getFluidOutputAccessFlags(), this.getBlock(), face);
	}


	/**
	 * Creates the block face config.
	 * 
	 * @return the config
	 */
	default BlockFaceConfig.Config createFluidOutputBlockFaceConfig() {
		return new BlockFaceConfig.Config("§dFluid Output Configuration", Holder.create(this::getFluidOutputAccessFlags, this::setFluidOutputAccessFlags), true, Material.LIME_STAINED_GLASS_PANE);
	}


	/**
	 * Checks if this supplier can push fluids into adjacent fluid consumer.
	 * 
	 * @return if automation is enabled
	 */
	default boolean isAutoPushingFluid() {
		return (this.getFluidOutputAccessFlags() & 0x40) != 0;
	}


	/**
	 * Tries to push fluids to adjacent blocks. Checks first whether automated pushing is enabled.
	 * 
	 * @param block the block
	 * @return whether some fluid got successfully pushed
	 */
	default boolean tryPushFluid() {
		if(!this.isAutoPushingFluid())
			return false;

		for(BlockFace face : BlockUtils.cartesian) {
			if(!this.canAccessFluidOutputs(face))
				continue;

			Block target = this.getBlock().getRelative(face);
			FluidConsumer consumer = FluidConsumer.getFluidSupplier(target);
			if(consumer == null || !consumer.canAccessFluidInputs(face.getOppositeFace()))
				continue;

			if(this.removeFluid(Filter.any(), 100, fluid -> consumer.addFluid(fluid)))
				return true;
		}

		return false;
	}


	/**
	 * Removes a {@link Fluid} from the given data holder.
	 * 
	 * @param max the maximum amount of the fluid to be removed
	 * @return the removed fluid
	 */
	default Fluid removeFluid(int max) {
		Pair<FluidType, Integer> result = Supplier.supply(max, this.getFluidOutputs());
		return new Fluid(result.left, result.right);
	}


	/**
	 * Removes a {@link Fluid} of the given {@code type} from this supplier.
	 * 
	 * @param type the type of the fluid
	 * @param max  the maximum amount of the fluid to be removed
	 * @return the removed fluid
	 */
	default Fluid removeFluid(FluidType type, int max) {
		int amount = Supplier.supply(type, max, this.getFluidOutputs());
		return new Fluid(type, amount);
	}


	/**
	 * Removes a {@link Fluid} from this supplier with the filter.
	 * 
	 * @param filter the filter
	 * @param max    the maximum amount of the fluid to be removed
	 * @return the removed fluid
	 */
	default Fluid removeFluid(Filter<FluidType> filter, int max) {
		Pair<FluidType, Integer> result = Supplier.supply(_ -> max, filter, this.getFluidOutputs());
		return new Fluid(result.left, result.right);
	}


	/**
	 * Removes a {@link Fluid} from this supplier with the filter and passes it to the given consumer. The fluid that get's returned by the consumer will
	 * be passed back to the supplier.
	 * 
	 * @param filter   the filter
	 * @param max      the maximum amount of the fluid to be removed
	 * @param consumer the consumer
	 * @return if any fluids got transfered
	 */
	default boolean removeFluid(Filter<FluidType> filter, int max, UnaryOperator<Fluid> consumer) {
		return removeFluid(filter, max, this.getFluidOutputs(), consumer);
	}


	/**
	 * Removes a {@link Fluid} from the given list of {@link FluidProvider} with the given filter and passes it to the consumer. The fluid that get's
	 * returned by the consumer will be passed back to the list of supplier.
	 * 
	 * @param filter   the filter
	 * @param max      the maximum amount of the fluid to be removed
	 * @param outputs  the list of fluid provider
	 * @param consumer the consumer
	 * @return if any fluids got transfered
	 */
	static boolean removeFluid(Filter<FluidType> filter, int max, List<FluidProvider> outputs, UnaryOperator<Fluid> consumer) {
		return Supplier.supply(_ -> max, filter, outputs, pair -> {
			Fluid fluid = consumer.apply(new Fluid(pair.left, pair.right));
			return new Pair<FluidType, Integer>(fluid.getType(), fluid.getMillibuckets());
		});
	}


	/**
	 * Returns the {@link FluidSupplier} from the given block.
	 * 
	 * @param block the block
	 * @return the fluid supplier or null
	 */
	public static FluidSupplier getFluidSupplier(Block block) {
		AbstractCustomTileEntity<?, ?> tileEntity = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(block);
		return tileEntity instanceof FluidSupplier supplier ? supplier : null;
	}

}
