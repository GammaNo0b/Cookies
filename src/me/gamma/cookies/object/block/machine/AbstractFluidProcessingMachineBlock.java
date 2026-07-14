
package me.gamma.cookies.object.block.machine;


import me.gamma.cookies.object.tile.machine.AbstractFluidProcessingMachine;



public abstract class AbstractFluidProcessingMachineBlock<B extends AbstractFluidProcessingMachineBlock<B, T>, T extends AbstractFluidProcessingMachine<T, B>> extends AbstractFluidGeneratingMachineBlock<B, T> {

	public AbstractFluidProcessingMachineBlock(MachineTier tier) {
		super(tier);
	}


	/**
	 * Returns the number of input tanks.
	 * 
	 * @return the number of input tanks
	 */
	public abstract int getInputTanks();

	/**
	 * Returns the capacity of the input tanks.
	 * 
	 * @return the capacity of the input tanks
	 */
	public abstract int getInputCapacity();

}
