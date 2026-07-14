
package me.gamma.cookies.object.tile.machine;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.machine.AbstractFluidGeneratingMachineBlock;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidConsumer;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractFluidGeneratingMachine<T extends AbstractFluidGeneratingMachine<T, B>, B extends AbstractFluidGeneratingMachineBlock<B, T>> extends AbstractProcessingMachine<T, B> implements FluidSupplier {

	private byte fluidOutputAccessFlags = 0x3F;
	protected final Fluid[] outputTanks;

	public AbstractFluidGeneratingMachine(B customBlock, Block block) {
		super(customBlock, block);

		this.outputTanks = ArrayUtils.generate(customBlock.getOutputTanks(), _ -> new Fluid(FluidType.EMPTY), Fluid[]::new);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		FluidSupplier.super.load(chunk, data);

		for(int i = 0; i < this.outputTanks.length; ++i)
			PersistentDataUtils.getFluid(data, "fluidoutput" + i, this.outputTanks[i]);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		FluidSupplier.super.save(chunk, data);

		for(int i = 0; i < this.outputTanks.length; ++i)
			PersistentDataUtils.setFluid(data, "fluidoutput" + i, this.outputTanks[i]);

		return true;
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		configs.add(this.createFluidOutputBlockFaceConfig());
		super.listBlockFaceProperties(configs);
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
	public List<FluidProvider> getFluidOutputs() {
		return Arrays.stream(this.outputTanks).map(fluid -> FluidProvider.fromFluid(fluid, this.customBlock.getOutputCapacity())).toList();
	}


	@Override
	public void tick() {
		this.tryPushFluid();
		super.tick();
	}


	/**
	 * Tries to store the given fluid in the output tanks.
	 * 
	 * @param fluid the fluid to be stored
	 * @return the remaining fluid
	 */
	protected Fluid storeOutput(Fluid fluid) {
		if(fluid.isEmpty())
			return new Fluid(FluidType.EMPTY);

		return FluidConsumer.addFluid(fluid, this.getFluidOutputs());
	}


	/**
	 * Tries to store all fluids from the given list in the output tanks. Fluids that could not be stored remain in the list. Returns if all fluids were
	 * stored.
	 * 
	 * @param fluids the fluids to be stored
	 * @return if the outputs could be stored
	 */
	protected boolean storeOutputs(List<Fluid> fluids) {
		Iterator<Fluid> iterator = fluids.iterator();
		while(iterator.hasNext()) {
			Fluid fluid = iterator.next();
			if(fluid.isEmpty()) {
				iterator.remove();
				continue;
			}

			Fluid rest = this.storeOutput(fluid);
			if(rest.isEmpty())
				iterator.remove();
			else
				fluid.setMillibuckets(rest.getMillibuckets());
		}

		return fluids.isEmpty();
	}


	public static ItemStack createTankIcon(Fluid tank, int capacity, int layer, int layers) {
		if(tank.isEmpty())
			return FluidType.EMPTY.createIcon();

		int fill = (int) Math.round(1.0D * tank.getMillibuckets() * layers / capacity);
		return new ItemBuilder((fill > layer ? tank.getType() : FluidType.EMPTY).getIcon()).setName(tank.getType().getName()).addLore("§f" + tank.getMillibuckets() + "§7mb").build();
	}


	public static void updateTank(Inventory gui, int column, int columns, int row, int rows, Fluid tank, int capacity) {
		int offset = row * 9 + column;
		for(int r = 0; r < rows; ++r) {
			ItemStack icon = createTankIcon(tank, capacity, rows - r - 1, rows);
			for(int c = 0; c < columns; ++c)
				gui.setItem(offset + 9 * r + c, icon);
		}
	}

}
