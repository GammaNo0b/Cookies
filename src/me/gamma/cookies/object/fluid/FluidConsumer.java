
package me.gamma.cookies.object.fluid;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.gamma.cookies.object.Consumer;
import me.gamma.cookies.object.DataStorage;
import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.TileEntityStorage;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;



/**
 * Represents a block that can store fluids in it.
 * 
 * @author gamma
 *
 */
public interface FluidConsumer extends DataStorage {

	String KEY_FLUID_INPUT_ACCESS_FLAGS = "fluidinputaccessflags";

	@Override
	default boolean load(Chunk chunk, PersistentDataObject data) {
		Byte b = data.getByte(KEY_FLUID_INPUT_ACCESS_FLAGS);
		if(b == null)
			return false;

		this.setFluidInputAccessFlags(b);
		return true;
	}


	@Override
	default boolean save(Chunk chunk, PersistentDataObject data) {
		data.setByte(KEY_FLUID_INPUT_ACCESS_FLAGS, this.getFluidInputAccessFlags());

		return true;
	}


	/**
	 * Returns the block of this fluid consumer.
	 * 
	 * @return the block
	 */
	Block getBlock();

	/**
	 * Returns the list of {@link FluidProvider} of this fluid consumer to consume fluids.
	 * 
	 * @param holder the data holder
	 * @return the list of fluid providers
	 */
	List<FluidProvider> getFluidInputs();


	/**
	 * Returns the list of {@link FluidProvider} of this fluid consumer on the given face to consume fluids.
	 * 
	 * @param face the block face
	 * @return the list of fluid providers
	 */
	default List<FluidProvider> getFluidInputs(BlockFace face) {
		return this.canAccessFluidInputs(face) ? this.getFluidInputs() : new ArrayList<>();
	}


	/**
	 * Returns the flags for each side of the block to be able to accept fluids from that side. The first six bits correspond to the six different sides
	 * in {@link BlockUtils#cartesian}. The seventh bit controlls automatic fluid transfer.
	 * 
	 * @return the access flags
	 */
	byte getFluidInputAccessFlags();

	/**
	 * Sets the new block face flags.
	 * 
	 * @param flags the flags
	 * @see FluidSupplier#getFluidOutputAccessFlags()
	 */
	void setFluidInputAccessFlags(byte flags);


	/**
	 * Checks if this fluid consumer can accept fluids from the given block face.
	 * 
	 * @param face the block face
	 * @return if it can accept fluids
	 */
	default boolean canAccessFluidInputs(BlockFace face) {
		return BlockFaceConfigurable.isFaceEnabled(this.getFluidInputAccessFlags(), this.getBlock(), face);
	}


	/**
	 * Creates the block face config.
	 * 
	 * @return the config
	 */
	default BlockFaceConfig.Config createFluidInputBlockFaceConfig() {
		return new BlockFaceConfig.Config("§dFluid Input Configuration", Holder.create(this::getFluidInputAccessFlags, this::setFluidInputAccessFlags), true, Material.MAGENTA_STAINED_GLASS_PANE);
	}


	/**
	 * Checks if this fluid consumer can pull fluids from adjacent fluid providers.
	 * 
	 * @return if automation is enabled
	 */
	default boolean isAutoPullingFluids() {
		return (this.getFluidInputAccessFlags() & 0x40) != 0;
	}


	/**
	 * Tries to pull fluids from adjacent blocks. Checks first whether automated pulling is enabled.
	 * 
	 * @return whether some fluid got successfully pulled
	 */
	default boolean tryPullFluid() {
		if(!this.isAutoPullingFluids())
			return false;

		for(BlockFace face : BlockUtils.cartesian) {
			if(!this.canAccessFluidInputs(face))
				continue;

			Block target = this.getBlock().getRelative(face);
			FluidSupplier supplier = FluidSupplier.getFluidSupplier(target);
			if(supplier == null || !supplier.canAccessFluidOutputs(face.getOppositeFace()))
				continue;

			if(supplier.removeFluid(Filter.any(), 100, fluid -> FluidConsumer.this.addFluid(fluid)))
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
	 * Adds the given stack to tthis fluid consumer.
	 * 
	 * @param fluid the fluid to be consumed
	 * @return if the fluid that couldn't be consumed
	 */
	default Fluid addFluid(Fluid fluid) {
		return addFluid(fluid, this.getFluidInputs());
	}


	/**
	 * Adds the given fluid to this fluid consumer on the given facce.
	 * 
	 * @param face  the block face
	 * @param fluid the fluid
	 * @return the fluid that couldn't be consumed
	 */
	default Fluid addFluid(BlockFace face, Fluid fluid) {
		if(!this.canAccessFluidInputs(face))
			return fluid;

		return this.addFluid(fluid);
	}


	/**
	 * Returns the {@link FluidConsumer} from the given block.
	 * 
	 * @param block the block
	 * @return the fluid consumer or null
	 */
	public static FluidConsumer getFluidSupplier(Block block) {
		AbstractCustomTileEntity<?, ?> tileEntity = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(block);
		return tileEntity instanceof FluidConsumer consumer ? consumer : null;
	}

}
