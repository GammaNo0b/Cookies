
package me.gamma.cookies.object.tile.machine;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.machine.AbstractFluidProcessingMachineBlock;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidConsumer;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractFluidProcessingMachine<T extends AbstractFluidProcessingMachine<T, B>, B extends AbstractFluidProcessingMachineBlock<B, T>> extends AbstractFluidGeneratingMachine<T, B> implements FluidConsumer {

	private byte fluidInputAccessFlags = 0x3F;

	protected final Fluid[] inputTanks;

	public AbstractFluidProcessingMachine(B customBlock, Block block) {
		super(customBlock, block);

		this.inputTanks = ArrayUtils.generate(customBlock.getInputTanks(), _ -> new Fluid(FluidType.EMPTY), Fluid[]::new);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		FluidConsumer.super.load(chunk, data);

		for(int i = 0; i < this.inputTanks.length; ++i)
			PersistentDataUtils.getFluid(data, "fluidinput" + i, this.inputTanks[i]);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		FluidConsumer.super.save(chunk, data);

		for(int i = 0; i < this.inputTanks.length; ++i)
			PersistentDataUtils.setFluid(data, "fluidinput" + i, this.inputTanks[i]);

		return true;
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		configs.add(this.createFluidInputBlockFaceConfig());
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
	public List<FluidProvider> getFluidInputs() {
		return Arrays.stream(this.inputTanks).map(fluid -> FluidProvider.fromFluid(fluid, this.customBlock.getInputCapacity())).toList();
	}


	@Override
	public void tick() {
		this.tryPullFluid();
		super.tick();
	}


	/**
	 * Returns a map of useable fluids.
	 * 
	 * @param block the block
	 * @return the useable fluids
	 */
	protected Map<FluidType, Integer> getUseableItems() {
		Map<FluidType, Integer> fluids = new HashMap<>();

		for(FluidProvider provider : this.getFluidInputs()) {
			if(provider.isEmpty())
				continue;

			fluids.merge(provider.getType(), provider.amount(), Integer::sum);
		}

		return fluids;
	}


	/**
	 * Tries to consume all the fluids from the given list from the input tanks of the given block. Returns true if all fluids could be consumed and false
	 * otherwise. The list of fluids will contain the fluids not being consumed.
	 * 
	 * @param fluids the list of fluids to consume
	 * @return whether all fluids could be consumed
	 */
	protected boolean consumeInputs(List<Fluid> fluids) {
		Iterator<Fluid> iterator = fluids.iterator();
		while(iterator.hasNext()) {
			Fluid fluid = iterator.next();
			if(fluid.isEmpty()) {
				iterator.remove();
				continue;
			}

			int amount = fluid.getMillibuckets();

			for(FluidProvider provider : this.getFluidInputs()) {
				if(provider.isEmpty())
					continue;

				if(!provider.match(fluid.getType()))
					continue;

				amount -= provider.get(amount);
				if(amount <= 0)
					break;
			}

			if(amount <= 0)
				iterator.remove();
			else
				fluid.setMillibuckets(amount);
		}

		return fluids.isEmpty();
	}



}
