
package me.gamma.cookies.object.block.machine;


import me.gamma.cookies.object.tile.machine.AbstractFluidGeneratingMachine;



public abstract class AbstractFluidGeneratingMachineBlock<B extends AbstractFluidGeneratingMachineBlock<B, T>, T extends AbstractFluidGeneratingMachine<T, B>> extends AbstractProcessingMachineBlock<B, T> {

	public AbstractFluidGeneratingMachineBlock(MachineTier tier) {
		super(tier);
	}


	/**
	 * Returns the number of output tanks.
	 * 
	 * @return the number of output tanks
	 */
	public abstract int getOutputTanks();

	/**
	 * Returns the capacity of the output tanks.
	 * 
	 * @return the capacity of the output tanks
	 */
	public abstract int getOutputCapacity();

}
