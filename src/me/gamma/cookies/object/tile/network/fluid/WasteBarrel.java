
package me.gamma.cookies.object.tile.network.fluid;


import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.fluid.WasteBarrelBlock;
import me.gamma.cookies.object.fluid.FluidConsumer;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class WasteBarrel extends AbstractCustomTileEntity<WasteBarrel, WasteBarrelBlock> implements FluidConsumer {

	private byte fluidInputAccessFlags = 0x3F;

	public WasteBarrel(WasteBarrelBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		FluidConsumer.super.load(chunk, data);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		FluidConsumer.super.save(chunk, data);

		return true;
	}


	@Override
	public List<FluidProvider> getFluidInputs() {
		return List.of(new FluidProvider() {

			@Override
			public void setType(FluidType type) {}


			@Override
			public void remove(int amount) {}


			@Override
			public boolean match(FluidType type) {
				return true;
			}


			@Override
			public FluidType getType() {
				return FluidType.EMPTY;
			}


			@Override
			public int capacity() {
				return Integer.MAX_VALUE;
			}


			@Override
			public boolean canChangeType(FluidType type) {
				return true;
			}


			@Override
			public int amount() {
				return 0;
			}


			@Override
			public void add(FluidType type, int amount) {}

		});
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
	public WasteBarrel castTileEntity() {
		return this;
	}

}
