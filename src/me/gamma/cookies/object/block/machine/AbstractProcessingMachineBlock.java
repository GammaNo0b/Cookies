
package me.gamma.cookies.object.block.machine;


import me.gamma.cookies.object.tile.machine.AbstractProcessingMachine;



public abstract class AbstractProcessingMachineBlock<B extends AbstractProcessingMachineBlock<B, T>, T extends AbstractProcessingMachine<T, B>> extends AbstractGuiMachineBlock<B, T> {

	public AbstractProcessingMachineBlock(MachineTier tier) {
		super(tier);
	}


	/**
	 * Returns the slot to store the progress icon.
	 * 
	 * @return the slot
	 */
	public int getProgressSlot() {
		return MachineConstants.PROGRESS_SLOT;
	}

}
