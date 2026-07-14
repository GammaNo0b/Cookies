
package me.gamma.cookies.object.tile.network.fluid;


import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.EnderLinkedBlock;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidStorage;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.tile.network.EnderLinkedTileEntity;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class EnderTank extends EnderLinkedTileEntity<Fluid> implements FluidStorage {

	private static final int CAPACITY = 64000;

	private final Fluid fluid = new Fluid(FluidType.EMPTY);
	private byte fluidInputAccessFlags = 0x3F;
	private byte fluidOutputAccessFlags = 0x3F;

	public EnderTank(EnderLinkedBlock<Fluid> customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		FluidStorage.super.load(chunk, data);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		FluidStorage.super.save(chunk, data);

		return true;
	}


	@Override
	public List<FluidProvider> getFluidProviders() {
		return List.of(FluidProvider.fromFluid(fluid, CAPACITY));
	}


	@Override
	public byte getFluidInputAccessFlags() {
		return this.fluidInputAccessFlags;
	}


	@Override
	public void setFluidInputAccessFlags(byte flags) {
		this.fluidInputAccessFlags = flags;
	}


	@Override
	public byte getFluidOutputAccessFlags() {
		return this.fluidOutputAccessFlags;
	}


	@Override
	public void setFluidOutputAccessFlags(byte flags) {
		this.fluidOutputAccessFlags = flags;
	}

}
