
package me.gamma.cookies.object.fluid;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.Consumer;
import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.block.Cartesian;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.property.ByteProperty;
import me.gamma.cookies.util.BlockUtils;



/**
 * Represents a block that can store fluids in it.
 * 
 * @author gamma
 *
 */
public interface FluidConsumer extends Cartesian {

	/**
	 * Property to store the fluid input access flags in a block.
	 */
	ByteProperty FLUID_INPUT_ACCESS_FLAGS = new ByteProperty("fluidinputaccessflags");

	/**
	 * Returns the list of {@link FluidProvider} of the given data holder to consume fluids.
	 * 
	 * @param holder the data holder
	 * @return the list of fluid providers
	 */
	List<FluidProvider> getFluidInputs(PersistentDataHolder holder);


	/**
	 * Returns the list of {@link FluidProvider} of the given block on the given face to consume fluids.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return the list of fluid providers
	 */
	default List<FluidProvider> getFluidInputs(TileState block, BlockFace face) {
		return this.canAccessFluidInputs(block, face) ? this.getFluidInputs(block) : new ArrayList<>();
	}


	/**
	 * Returns the flags for each side of the block to be able to accept fluids from that side. The first six bits correspond to the six different sides
	 * in {@link BlockUtils#cartesian}. The seventh bit controlls automatic fluid transfer.
	 * 
	 * @param holder the data holder
	 * @return the access flags
	 */
	default byte getFluidInputAccessFlags(PersistentDataHolder holder) {
		return FLUID_INPUT_ACCESS_FLAGS.fetch(holder);
	}


	/**
	 * Checks if the given block can accept fluids from the given block face.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return if it can accept fluids
	 */
	default boolean canAccessFluidInputs(TileState block, BlockFace face) {
		return BlockFaceConfigurable.isFaceEnabled(this.getFluidInputAccessFlags(block), block, face);
	}


	/**
	 * Creates the block face config.
	 * 
	 * @return the config
	 */
	default BlockFaceConfig.Config createFluidInputBlockFaceConfig() {
		return new BlockFaceConfig.Config("§dFluid Input Configuration", FLUID_INPUT_ACCESS_FLAGS, true, Material.MAGENTA_STAINED_GLASS_PANE);
	}


	/**
	 * Checks if the given data holder can pull fluids from adjacent fluid providers.
	 * 
	 * @param holder the data holder
	 * @return if automation is enabled
	 */
	default boolean isAutoPullingFluids(PersistentDataHolder holder) {
		return (this.getFluidInputAccessFlags(holder) & 0x40) != 0;
	}


	/**
	 * Tries to pull fluids from adjacent blocks. Checks first whether automated pulling is enabled.
	 * 
	 * @param block the block
	 * @return whether some fluid got successfully pulled
	 */
	default boolean tryPullFluid(TileState block) {
		if(!this.isAutoPullingFluids(block))
			return false;

		for(BlockFace face : BlockUtils.cartesian) {
			if(!this.canAccessFluidInputs(block, face))
				continue;

			Block target = block.getBlock().getRelative(face);
			if(!(target.getState() instanceof TileState tile))
				continue;

			FluidSupplier supplier = FluidSupplier.getFluidSupplier(tile);
			if(supplier == null || !supplier.canAccessFluidOutputs(tile, face.getOppositeFace()))
				continue;

			if(supplier.removeFluid(tile, Filter.empty(), 100, fluid -> FluidConsumer.this.addFluid(block, fluid)))
				return true;
		}

		return false;
	}


	/**
	 * Adds the given stack to the list of {@link FluidProvider}.
	 * 
	 * @param fluid  the stack to be consumed
	 * @param inputs the list of fluid provider
	 * @return the fluid that couldn't be consumed
	 */
	static Fluid addFluid(Fluid fluid, List<FluidProvider> inputs) {
		int amount = Consumer.consume(fluid.getType(), fluid.getMillibuckets(), inputs);
		return new Fluid(fluid.getType(), amount);
	}


	/**
	 * Adds the given stack to the given data holder.
	 * 
	 * @param holder the data holder
	 * @param fluid  the fluid to be consumed
	 * @return if the fluid that couldn't be consumed
	 */
	default Fluid addFluid(PersistentDataHolder holder, Fluid fluid) {
		return addFluid(fluid, this.getFluidInputs(holder));
	}


	/**
	 * Adds the given fluid to the given block on the given facce.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @param fluid the fluid
	 * @return the fluid that couldn't be consumed
	 */
	default Fluid addFluid(TileState block, BlockFace face, Fluid fluid) {
		if(!this.canAccessFluidInputs(block, face))
			return fluid;

		return this.addFluid(block, fluid);
	}


	/**
	 * Returns the {@link FluidConsumer} from the given data holder.
	 * 
	 * @param holder the data holder
	 * @return the fluid consumer or null
	 */
	public static FluidConsumer getFluidConsumer(PersistentDataHolder holder) {
		AbstractCustomBlock custom = Blocks.getCustomBlockFromHolder(holder);
		return custom instanceof FluidConsumer ? (FluidConsumer) custom : null;
	}

}
