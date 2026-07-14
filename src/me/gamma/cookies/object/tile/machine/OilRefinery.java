
package me.gamma.cookies.object.tile.machine;


import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.block.machine.OilRefineryBlock;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidConsumer;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class OilRefinery extends AbstractGuiMachine<OilRefinery, OilRefineryBlock> implements FluidConsumer, FluidSupplier {

	private static final int TANK_CAPACITY = 4000;

	private static final String KEY_CRUDE_OIL = "crudeoil";
	private static final String KEY_REFINED_OIL = "refinedoil";

	private static final Random r = new Random();

	private byte fluidInputAccessFlags = 0x3F;
	private byte fluidOutputAccessFlags = 0x3F;

	private int crudeOil = 0;
	private int refinedOil = 0;

	public OilRefinery(OilRefineryBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		FluidConsumer.super.load(chunk, data);
		FluidSupplier.super.load(chunk, data);

		this.crudeOil = data.getInteger(KEY_CRUDE_OIL, 0);
		this.refinedOil = data.getInteger(KEY_REFINED_OIL, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;
		FluidConsumer.super.save(chunk, data);
		FluidSupplier.super.save(chunk, data);

		data.setInteger(KEY_CRUDE_OIL, this.crudeOil);
		data.setInteger(KEY_REFINED_OIL, this.refinedOil);

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
		return List.of(FluidProvider.fromHolder(FluidType.CRUDE_OIL, Holder.create(() -> OilRefinery.this.crudeOil, i -> { OilRefinery.this.crudeOil = i; }), TANK_CAPACITY));
	}


	@Override
	public List<FluidProvider> getFluidOutputs() {
		return List.of(FluidProvider.fromHolder(FluidType.REFINED_OIL, Holder.create(() -> OilRefinery.this.refinedOil, i -> { OilRefinery.this.refinedOil = i; }), TANK_CAPACITY));
	}


	@Override
	protected boolean run() {
		if(this.crudeOil <= 0)
			return false;

		--this.crudeOil;
		if(r.nextDouble() < this.customBlock.getRefiningEfficiency())
			++this.refinedOil;

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
		AbstractFluidGeneratingMachine.updateTank(this.getInventory(), 1, 2, 1, 4, new Fluid(FluidType.CRUDE_OIL, this.crudeOil), TANK_CAPACITY);
		AbstractFluidGeneratingMachine.updateTank(this.getInventory(), 6, 2, 1, 4, new Fluid(FluidType.REFINED_OIL, this.refinedOil), TANK_CAPACITY);
	}


	@Override
	public OilRefinery castTileEntity() {
		return this;
	}

}
