
package me.gamma.cookies.object.tile.machine;


import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.block.machine.OilPumpBlock;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidConsumer;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class OilPump extends AbstractGuiMachine<OilPump, OilPumpBlock> implements FluidConsumer, FluidSupplier {

	private static final int WATER_CAPACITY = 10000;
	private static final int OIL_CAPACITY = 1000;

	private static final String KEY_WATER = "water";
	private static final String KEY_OIL = "oil";

	private static final Random r = new Random();

	private byte fluidInputAccessFlags = 0x3F;
	private byte fluidOutputAccessFlags = 0x3F;

	private int water;
	private int oil;

	public OilPump(OilPumpBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		FluidConsumer.super.load(chunk, data);
		FluidSupplier.super.load(chunk, data);

		this.water = data.getInteger(KEY_WATER, 0);
		this.oil = data.getInteger(KEY_OIL, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;
		FluidConsumer.super.save(chunk, data);
		FluidSupplier.super.save(chunk, data);

		data.setInteger(KEY_WATER, this.water);
		data.setInteger(KEY_OIL, this.oil);

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);

		this.updateTanks();
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		configs.add(this.createFluidInputBlockFaceConfig());
		configs.add(this.createFluidOutputBlockFaceConfig());
		super.listBlockFaceProperties(configs);
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


	@Override
	public List<FluidProvider> getFluidInputs() {
		return List.of(FluidProvider.fromHolder(FluidType.WATER, Holder.create(() -> OilPump.this.water, i -> { OilPump.this.water = i; }), WATER_CAPACITY));
	}


	@Override
	public List<FluidProvider> getFluidOutputs() {
		return List.of(FluidProvider.fromHolder(FluidType.CRUDE_OIL, Holder.create(() -> OilPump.this.oil, i -> { OilPump.this.oil = i; }), OIL_CAPACITY));
	}


	@Override
	protected boolean run() {
		if(this.water < this.customBlock.getWaterConsumption() || this.oil >= OIL_CAPACITY)
			return false;

		this.water -= this.customBlock.getWaterConsumption();
		if(r.nextInt(this.customBlock.getOilAttempts()) == 0)
			++this.oil;

		return true;
	}


	@Override
	public void tick() {
		this.tryPullFluid();

		super.tick();

		this.tryPushFluid();

		this.updateTanks();
	}


	private void updateTanks() {
		AbstractFluidGeneratingMachine.updateTank(this.getInventory(), 1, 2, 1, 4, new Fluid(FluidType.WATER, this.water), WATER_CAPACITY);
		AbstractFluidGeneratingMachine.updateTank(this.getInventory(), 6, 2, 1, 4, new Fluid(FluidType.CRUDE_OIL, this.oil), OIL_CAPACITY);
	}


	@Override
	public OilPump castTileEntity() {
		return this;
	}

}
