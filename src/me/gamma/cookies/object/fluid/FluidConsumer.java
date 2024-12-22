
package me.gamma.cookies.object.fluid;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;

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
	 * Returns the list of {@link FluidProvider} of the given block to consume fluids.
	 * 
	 * @param block the block
	 * @return the list of fluid providers
	 */
	List<FluidProvider> getFluidInputs(TileState block);


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
	 * in {@link BlockUtils#cartesian}.
	 * 
	 * @param block the block
	 * @return the access flags
	 */
	default byte getFluidInputAccessFlags(TileState block) {
		return FLUID_INPUT_ACCESS_FLAGS.fetch(block);
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
	 * Checks if the given block can pull fluids from adjacent fluid providers.
	 * 
	 * @param block the block
	 * @return if automation is enabled
	 */
	default boolean isAutoPullingFluids(TileState block) {
		return (this.getFluidInputAccessFlags(block) & 0x40) != 0;
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
	 * Adds the given stack to the given block.
	 * 
	 * @param block the block
	 * @param fluid the fluid to be consumed
	 * @return if the fluid that couldn't be consumed
	 */
	default Fluid addFluid(TileState block, Fluid fluid) {
		return this.addFluid(block, null, false, fluid);
	}


	/**
	 * Adds the given fluid to the given block on the given facce.
	 * 
	 * @param block       the block
	 * @param face        the block face
	 * @param checkAccess whether the access check should be done
	 * @param fluid       the fluid
	 * @return the fluid that couldn't be consumed
	 */
	default Fluid addFluid(TileState block, BlockFace face, boolean checkAccess, Fluid fluid) {
		return checkAccess && !this.canAccessFluidInputs(block, face) ? fluid : addFluid(fluid, this.getFluidInputs(block));
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
	 * Returns the {@link FluidConsumer} from the given block.
	 * 
	 * @param block the block
	 * @return the fluid consumer or null
	 */
	public static FluidConsumer getFluidConsumer(TileState block) {
		AbstractCustomBlock custom = Blocks.getCustomBlockFromBlock(block);
		return custom instanceof FluidConsumer ? (FluidConsumer) custom : null;
	}

}
