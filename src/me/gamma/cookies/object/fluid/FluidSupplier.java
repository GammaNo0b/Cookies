
package me.gamma.cookies.object.fluid;


import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.Supplier;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.block.Cartesian;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.property.ByteProperty;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.collection.Pair;



/**
 * Represents a block that can provide fluids from it.
 * 
 * @author gamma
 *
 */
public interface FluidSupplier extends Cartesian {

	/**
	 * Property to store the fluid input access flags in a block.
	 */
	ByteProperty FLUID_OUTPUT_ACCESS_FLAGS = new ByteProperty("fluidoutputaccessflags");

	/**
	 * Returns the list of {@link FluidProvider} of the given data holder to supply fluids.
	 * 
	 * @param holder the data holder
	 * @return the list of fluid providers
	 */
	List<FluidProvider> getFluidOutputs(PersistentDataHolder holder);


	/**
	 * Returns the list of {@link FluidProvider} of the given block on the given face to supply fluids.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return the list of fluid providers
	 */
	default List<FluidProvider> getFluidOutputs(TileState block, BlockFace face) {
		return this.canAccessFluidOutputs(block, face) ? this.getFluidOutputs(block) : new ArrayList<>();
	}


	/**
	 * Returns the flags for each side of the block to be able to yield fluids from that side. The first six bits correspond to the six different sides in
	 * {@link BlockUtils#cartesian}. The seventh bit controlls automatic fluid transfer.
	 * 
	 * @param holder the data holder
	 * @return the access flags
	 */
	default byte getFluidOutputAccessFlags(PersistentDataHolder holder) {
		return FLUID_OUTPUT_ACCESS_FLAGS.fetch(holder);
	}


	/**
	 * Checks if the given block can yield fluids from the given block face.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return if it can accept fluids
	 */
	default boolean canAccessFluidOutputs(TileState block, BlockFace face) {
		return BlockFaceConfigurable.isFaceEnabled(this.getFluidOutputAccessFlags(block), block, face);
	}


	/**
	 * Creates the block face config.
	 * 
	 * @return the config
	 */
	default BlockFaceConfig.Config createFluidOutputBlockFaceConfig() {
		return new BlockFaceConfig.Config("§dFluid Output Configuration", FLUID_OUTPUT_ACCESS_FLAGS, true, Material.LIME_STAINED_GLASS_PANE);
	}


	/**
	 * Checks if the given data holder can push fluids into adjacent fluid consumer.
	 * 
	 * @param holder the data holder
	 * @return if automation is enabled
	 */
	default boolean isAutoPushingFluid(PersistentDataHolder holder) {
		return (this.getFluidOutputAccessFlags(holder) & 0x40) != 0;
	}


	/**
	 * Tries to push fluids to adjacent blocks. Checks first whether automated pushing is enabled.
	 * 
	 * @param block the block
	 * @return whether some fluid got successfully pushed
	 */
	default boolean tryPushFluid(TileState block) {
		if(!this.isAutoPushingFluid(block))
			return false;

		for(BlockFace face : BlockUtils.cartesian) {
			if(!this.canAccessFluidOutputs(block, face))
				continue;

			Block target = block.getBlock().getRelative(face);
			if(!(target.getState() instanceof TileState tile))
				continue;

			FluidConsumer consumer = FluidConsumer.getFluidConsumer(tile);
			if(consumer == null || !consumer.canAccessFluidInputs(tile, face.getOppositeFace()))
				continue;

			if(this.removeFluid(block, Filter.empty(), 100, fluid -> consumer.addFluid(tile, fluid)))
				return true;
		}

		return false;
	}


	/**
	 * Removes a {@link Fluid} from the given data holder.
	 * 
	 * @param holder the data holder
	 * @param max    the maximum amount of the fluid to be removed
	 * @return the removed fluid
	 */
	default Fluid removeFluid(PersistentDataHolder holder, int max) {
		Pair<FluidType, Integer> result = Supplier.supply(max, this.getFluidOutputs(holder));
		return new Fluid(result.left, result.right);
	}


	/**
	 * Removes a {@link Fluid} of the given {@code type} from the given data holder.
	 * 
	 * @param holder the data holder
	 * @param type   the type of the fluid
	 * @param max    the maximum amount of the fluid to be removed
	 * @return the removed fluid
	 */
	default Fluid removeFluid(PersistentDataHolder holder, FluidType type, int max) {
		int amount = Supplier.supply(type, max, this.getFluidOutputs(holder));
		return new Fluid(type, amount);
	}


	/**
	 * Removes a {@link Fluid} from the given data holder with the filter.
	 * 
	 * @param holder the data holder
	 * @param filter the filter
	 * @param max    the maximum amount of the fluid to be removed
	 * @return the removed fluid
	 */
	default Fluid removeFluid(PersistentDataHolder holder, Filter<FluidType> filter, int max) {
		Pair<FluidType, Integer> result = Supplier.supply(_ -> max, filter, this.getFluidOutputs(holder));
		return new Fluid(result.left, result.right);
	}


	/**
	 * Removes a {@link Fluid} from the given data holder with the filter and passes it to the given consumer. The fluid that get's returned by the
	 * consumer will be passed back to the supplier.
	 * 
	 * @param holder   the data holder
	 * @param filter   the filter
	 * @param max      the maximum amount of the fluid to be removed
	 * @param consumer the consumer
	 * @return if any fluids got transfered
	 */
	default boolean removeFluid(PersistentDataHolder holder, Filter<FluidType> filter, int max, UnaryOperator<Fluid> consumer) {
		return removeFluid(filter, max, this.getFluidOutputs(holder), consumer);
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
	 * Returns the {@link FluidSupplier} of the given data holder.
	 * 
	 * @param holder the data holder
	 * @return the supplier or null
	 */
	static FluidSupplier getFluidSupplier(PersistentDataHolder holder) {
		AbstractCustomBlock custom = Blocks.getCustomBlockFromHolder(holder);
		return custom instanceof FluidSupplier ? (FluidSupplier) custom : null;
	}

}
