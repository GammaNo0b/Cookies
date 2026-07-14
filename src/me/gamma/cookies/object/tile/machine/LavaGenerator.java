
package me.gamma.cookies.object.tile.machine;


import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.RedstoneMode;
import me.gamma.cookies.object.block.Switchable;
import me.gamma.cookies.object.block.machine.LavaGeneratorBlock;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class LavaGenerator extends AbstractCustomTileEntity<LavaGenerator, LavaGeneratorBlock> implements FluidSupplier, Switchable {

	private static final String KEY_REDSTONE_MODE = "mode";
	private static final String KEY_LAVA = "lava";
	private static final String KEY_LAVA_TICKS = "lavaticks";

	private byte fluidOutputAccessFlags = 0x3f;
	private RedstoneMode mode = RedstoneMode.REDSTONE_ON;
	private int lava = 0;
	private int lavaTicks = 0;

	public LavaGenerator(LavaGeneratorBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		FluidSupplier.super.load(chunk, data);

		this.mode = PersistentDataUtils.getEnum(data, KEY_REDSTONE_MODE, RedstoneMode.class);
		if(this.mode == null)
			return false;

		this.lava = data.getInteger(KEY_LAVA, 0);
		this.lavaTicks = data.getInteger(KEY_LAVA_TICKS, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		FluidSupplier.super.save(chunk, data);

		PersistentDataUtils.setEnum(data, KEY_REDSTONE_MODE, this.mode);
		data.setInteger(KEY_LAVA, this.lava);
		data.setInteger(KEY_LAVA_TICKS, this.lavaTicks);

		return true;
	}


	@Override
	public RedstoneMode getRedstoneMode() {
		return this.mode;
	}


	@Override
	public boolean isTicking() {
		return true;
	}


	@Override
	public void tick() {
		if(--this.lavaTicks > 0)
			return;

		this.lavaTicks = this.customBlock.getFrequency();
		this.lava = Math.min(this.lava + this.customBlock.getGeneration(), this.customBlock.getCapacity());
		this.tryPushFluid();
	}


	public int getLava() {
		return this.lava;
	}


	public boolean removeLava(int amount) {
		if(this.lava < amount)
			return false;

		this.lava -= amount;
		return true;
	}


	@Override
	public List<FluidProvider> getFluidOutputs() {
		return List.of(FluidProvider.fromHolder(FluidType.LAVA, Holder.create(() -> this.lava, i -> { this.lava = i; }), this.customBlock.getCapacity()));
	}


	@Override
	public byte getFluidOutputAccessFlags() {
		return this.fluidOutputAccessFlags;
	}


	@Override
	public void setFluidOutputAccessFlags(byte flags) {
		this.fluidOutputAccessFlags = flags;
	}


	@Override
	public LavaGenerator castTileEntity() {
		return this;
	}

}
